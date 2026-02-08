package com.example.player.player

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.BasePlayerSettingsVM
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.VideoQuality
import com.example.data.models.releases.anime_details.Episode
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerVM @Inject constructor(
    val uiPlayer: ExoPlayer,
    private val controllerFuture: ListenableFuture<MediaController>,
    private val playerSettingsRepo: PlayerSettingsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
    @param:Dispatcher(LibertyFlowDispatcher.Main) private val dispatcherMain: CoroutineDispatcher
) : BasePlayerSettingsVM(playerSettingsRepo, dispatcherIo) {

    // --- State ---
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.toLazily(PlayerState())

    // --- Controller & Jobs ---
    private var mediaController: MediaController? = null
    private var progressJob: Job? = null
    private var controllerVisibilityJob: Job? = null

    init {
        initializeController()
        observeSettings()
    }

    private fun initializeController() {
        viewModelScope.launch(dispatcherMain) {
            runCatching {
                controllerFuture.asDeferred().await()
            }.onSuccess { controller ->
                mediaController = controller
                controller.addListener(playerListener)
            }.onFailure { it.printStackTrace() }
        }
    }

    // --- Intents ---
    fun sendIntent(intent: PlayerIntent) {
        val controller = mediaController ?: return

        when (intent) {
            // Lifecycle
            is PlayerIntent.SetUpPlayer -> setUpPlayer(intent)

            // Playback
            PlayerIntent.TogglePlayPause -> togglePlayPause(controller)
            PlayerIntent.StopPlayer -> stopPlayer(controller)
            PlayerIntent.SkipOpening -> skipOpening(controller)

            // Settings affecting Playback
            is PlayerIntent.SaveQuality -> saveVideoQuality(intent.quality)

            // UI State (Locally handled)
            is PlayerIntent.SetIsScrubbing -> updateState { it.setIsScrubbing(intent.value) }
            PlayerIntent.ToggleControllerVisible -> toggleControllerVisibility()
            PlayerIntent.TurnOffController -> updateState { it.setControllerVisible(false) }
            PlayerIntent.ToggleFullScreen -> toggleFullScreen()
            PlayerIntent.ToggleEpisodesDialog -> updateState { it.toggleEpisodesDialog() }
            PlayerIntent.ToggleSettingsBS -> updateState { it.toggleSettingsBS() }
            PlayerIntent.ToggleQualityBS -> updateState { it.toggleQualityBS() }
            PlayerIntent.ToggleCropped -> updateState { it.toggleIsCropped() }
            PlayerIntent.ToggleIsLocked -> updateState { it.toggleIsLocked() }

            // Preferences Toggles
            PlayerIntent.ToggleAutoPlay -> toggleAutoPlay(!currentState.playerSettings.autoPlay)
            PlayerIntent.ToggleAutoSkipOpening -> toggleAutoSkipOpening(!currentState.playerSettings.autoSkipOpening)
            PlayerIntent.ToggleShowSkipOpeningButton -> toggleShowSkipOpeningButton(!currentState.playerSettings.showSkipOpeningButton)
        }
    }

    // --- Effects (Commands) ---
    fun sendEffect(effect: PlayerEffect) {
        val controller = mediaController ?: return

        when (effect) {
            is PlayerEffect.SeekForFiveSeconds -> seekRelative(controller, if (effect.forward) 5000L else -5000L)
            is PlayerEffect.SkipEpisode -> changeMediaItem(controller, if (effect.forward) 1 else -1)
            is PlayerEffect.ChangeEpisode -> controller.seekTo(effect.index, 0L)
            is PlayerEffect.SeekTo -> controller.seekTo(effect.position)
        }
    }

    // --- Logic Implementation ---
    private fun setUpPlayer(intent: PlayerIntent.SetUpPlayer) {
        viewModelScope.launch(dispatcherMain) {
            val controller = mediaController ?: controllerFuture.asDeferred().await().also { mediaController = it }

            val settings = playerSettingsRepo.playerSettings.first()

            // UI Update
            updateState {
                it.copy(
                    animeName = intent.animeName,
                    uiPlayerState = PlayerState.UiPlayerState.Mini,
                    episodes = intent.episodes,
                    currentEpisodeIndex = intent.startIndex,
                    playerSettings = settings,
                    episodeState = PlayerState.EpisodeState.Loading
                )
            }

            // Command Execution via Controller
            withContext(dispatcherMain) {
                val mediaItems = intent.episodes.map { it.toMediaItem(settings.quality, intent.animeName) }

                controller.setMediaItems(mediaItems, intent.startIndex, 0L)
                controller.prepare()
                controller.playWhenReady = settings.autoPlay

                startProgressTracker(controller)
            }
        }
    }

    private fun togglePlayPause(controller: MediaController) =
        if (controller.isPlaying) controller.pause() else controller.play()

    private fun stopPlayer(controller: MediaController) {
        progressJob?.cancel()
        controller.stop()
        controller.clearMediaItems()
        updateState { PlayerState() }
    }

    private fun seekRelative(controller: MediaController, offsetMs: Long) =
        controller.seekTo(controller.currentPosition + offsetMs)

    private fun changeMediaItem(controller: MediaController, offset: Int) =
        if (offset > 0) controller.seekToNextMediaItem() else controller.seekToPreviousMediaItem()

    private fun skipOpening(controller: MediaController) {
        val endMs = (currentState.currentEpisode?.opening?.end?.toLong() ?: 0L) * 1000
        controller.seekTo(endMs)
    }

    // --- Settings & Quality Change ---
    private fun observeSettings() {
        viewModelScope.launch(dispatcherIo) {
            playerSettingsRepo.playerSettings.collect { newSettings ->
                val oldSettings = currentState.playerSettings
                updateState { it.copy(playerSettings = newSettings) }

                if (oldSettings.quality != newSettings.quality && mediaController != null) {
                    changeQualityOnTheFly(mediaController!!, newSettings.quality)
                }
            }
        }
    }

    private suspend fun changeQualityOnTheFly(controller: MediaController, quality: VideoQuality) = withContext(dispatcherMain) {
        val currentPos = controller.currentPosition
        val currentIndex = controller.currentMediaItemIndex
        val wasPlaying = controller.isPlaying

        val animeName = currentState.animeName
        val newItems = currentState.episodes.map { it.toMediaItem(quality, animeName) }

        controller.setMediaItems(newItems, currentIndex, currentPos)
        controller.prepare()
        if (wasPlaying) controller.play()
    }

    // --- Progress Tracking (Reading from Controller) ---
    private fun startProgressTracker(controller: MediaController) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch(dispatcherMain) {
            while (isActive) {
                val currentPos = controller.currentPosition
                val duration = controller.duration.coerceAtLeast(0L)

                updateState { it.updateDuration(currentPos, duration) }

                checkOpeningLogic(controller, currentPos)
                delay(500)
            }
        }
    }

    private fun checkOpeningLogic(controller: MediaController, currentPos: Long) {
        val episode = currentState.currentEpisode ?: return
        val opening = episode.opening

        val startMs = (opening.start ?: -1) * 1000L
        val endMs = (opening.end ?: -1) * 1000L

        if (currentPos in startMs..endMs) {
            if (currentState.playerSettings.autoSkipOpening) {
                controller.seekTo(endMs)
            } else if (currentState.playerSettings.showSkipOpeningButton) {
                if (!currentState.isSkipOpeningButtonVisible) {
                    updateState { it.copy(isSkipOpeningButtonVisible = true) }
                }
            }
        } else {
            if (currentState.isSkipOpeningButtonVisible) {
                updateState { it.copy(isSkipOpeningButtonVisible = false) }
            }
        }
    }

    // --- Listeners ---
    private val playerListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaController?.let { ctrl ->
                updateState { it.copy(currentEpisodeIndex = ctrl.currentMediaItemIndex) }
            }
        }

        override fun onPlaybackStateChanged(state: Int) {
            val isPlaying = mediaController?.isPlaying == true
            val newState = when (state) {
                Player.STATE_BUFFERING -> PlayerState.EpisodeState.Loading
                Player.STATE_READY -> if (isPlaying) PlayerState.EpisodeState.Playing else PlayerState.EpisodeState.Paused
                Player.STATE_ENDED -> PlayerState.EpisodeState.Paused
                else -> PlayerState.EpisodeState.Paused
            }
            updateState { it.copy(episodeState = newState) }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val state = if (isPlaying) PlayerState.EpisodeState.Playing else PlayerState.EpisodeState.Paused
            if (mediaController?.playbackState == Player.STATE_READY) {
                updateState { it.copy(episodeState = state) }
            }
        }
    }

    // --- Helpers ---
    private fun toggleControllerVisibility() {
        controllerVisibilityJob?.cancel()
        val nextState = !currentState.isControllerVisible
        updateState { it.setControllerVisible(nextState) }
        if (nextState && !currentState.isLocked) {
            controllerVisibilityJob = viewModelScope.launch {
                delay(4000)
                updateState { it.setControllerVisible(false) }
            }
        }
    }

    private fun toggleFullScreen() {
        updateState {
            val next = if (it.uiPlayerState == PlayerState.UiPlayerState.Full)
                PlayerState.UiPlayerState.Mini else PlayerState.UiPlayerState.Full
            it.copy(uiPlayerState = next)
        }
    }

    private val currentState: PlayerState get() = _playerState.value

    private fun updateState(function: (PlayerState) -> PlayerState) {
        _playerState.update(function)
    }

    override fun onCleared() {
        super.onCleared()
        mediaController?.release()
        MediaController.releaseFuture(controllerFuture)
    }
}

private const val NO_TITLE_PROVIDED = "No title provided"

// Just for fun :)
private val ArtworksList = listOf(
    "https://i.pinimg.com/736x/b9/60/64/b96064e99125509c7b0b77425520c5ae.jpg",
    "https://i.pinimg.com/736x/d0/97/f0/d097f040b61e0a223c7ccb2eb8982848.jpg",
    "https://i.pinimg.com/1200x/f7/a3/5f/f7a35fb05ccccc861cfbdaab7c3603c5.jpg",
    "https://i.pinimg.com/1200x/4f/64/89/4f6489d77796bc8381ef3477a19e16c9.jpg",
    "https://i.pinimg.com/736x/86/e2/1f/86e21fa28ccbea7f058338b101b3ba78.jpg",
    "https://i.pinimg.com/736x/2b/5c/28/2b5c28602c81ec0de64d3098972da411.jpg",
    "https://i.pinimg.com/736x/24/eb/aa/24ebaada5139f61e22340654392f819d.jpg"
)

// --- Mappers ---
private fun Episode.toMediaItem(quality: VideoQuality, animeName: String): MediaItem {
    val uri = when(quality) {
        VideoQuality.SD -> hls480
        VideoQuality.HD -> hls720 ?: hls480
        VideoQuality.FHD -> hls1080 ?: hls720 ?: hls480
    }

    return MediaItem.Builder()
        .setUri(uri)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(name ?: NO_TITLE_PROVIDED)
                .setArtist(animeName)
                .setArtworkUri(ArtworksList.random().toUri())
                .build()
        )
        .build()
}
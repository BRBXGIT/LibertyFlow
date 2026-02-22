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
import com.example.common.vm_helpers.player.BasePlayerSettingsVM
import com.example.common.vm_helpers.utils.toLazily
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.domain.WatchedEpsRepo
import com.example.data.models.player.VideoQuality
import com.example.data.models.releases.anime_details.Episode
import com.example.data.player.SleepTimerManager
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Add doc
@HiltViewModel
class PlayerVM @Inject constructor(
    val uiPlayer: ExoPlayer, // Inject only for passing to ui
    private val controllerFuture: ListenableFuture<MediaController>,
    private val playerSettingsRepo: PlayerSettingsRepo,
    private val watchedEpsRepo: WatchedEpsRepo,
    private val sleepTimerManager: SleepTimerManager,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
    @param:Dispatcher(LibertyFlowDispatcher.Main) private val dispatcherMain: CoroutineDispatcher
): BasePlayerSettingsVM(playerSettingsRepo, dispatcherIo) {

    // --- State ---
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.toLazily(PlayerState())

    private val _playerEffects = Channel<PlayerEffect>(Channel.BUFFERED)
    val playerEffects = _playerEffects.receiveAsFlow()

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
            is PlayerIntent.SeekForFiveSeconds -> seekRelative(controller, if (intent.forward) 5000L else -5000L)
            is PlayerIntent.SkipEpisode -> changeMediaItem(controller, if (intent.forward) 1 else -1)
            is PlayerIntent.ChangeEpisode -> controller.seekTo(intent.index, 0L)
            is PlayerIntent.SeekTo -> controller.seekTo(intent.position)
            PlayerIntent.TogglePlayPause -> togglePlayPause(controller)
            PlayerIntent.StopPlayer -> stopPlayer(controller)
            PlayerIntent.SkipOpening -> skipOpening(controller)

            // Sleep timer
            is PlayerIntent.SetSleepTimer -> setSleepTimer(intent.minutes, intent.seconds)

            // Settings affecting Playback
            is PlayerIntent.SaveQuality -> saveVideoQuality(intent.quality)

            // UI State (Locally handled)
            is PlayerIntent.SetIsScrubbing -> updateState { it.setIsScrubbing(intent.value) }
            PlayerIntent.ToggleControllerVisible -> toggleControllerVisibility()
            PlayerIntent.TurnOffController -> updateState { it.setControllerVisible(false) }
            PlayerIntent.ToggleFullScreen -> toggleFullScreen()
            PlayerIntent.ToggleEpisodesDialog -> updateState { it.toggleEpisodesDialog() }
            PlayerIntent.ToggleSettingsDialog -> updateState { it.toggleSettingsBS() }
            PlayerIntent.ToggleQualityDialog -> updateState { it.toggleQualityBS() }
            PlayerIntent.ToggleIsLocked -> updateState { it.toggleIsLocked() }

            // Preferences Toggles
            PlayerIntent.ToggleAutoPlay -> toggleAutoPlay(!currentState.playerSettings.autoPlay)
            PlayerIntent.ToggleAutoSkipOpening -> toggleAutoSkipOpening(!currentState.playerSettings.autoSkipOpening)
            PlayerIntent.ToggleShowSkipOpeningButton -> toggleShowSkipOpeningButton(!currentState.playerSettings.showSkipOpeningButton)
            PlayerIntent.ToggleCropped -> toggleIsCropped(!currentState.playerSettings.isCropped)
        }
    }

    // --- Effects (Commands) ---
    fun sendEffect(effect: PlayerEffect) =
        viewModelScope.launch { _playerEffects.send(effect) }

    // --- Logic Implementation ---
    /**
     * Initializes the player with a list of episodes, configures the media items
     * based on user quality settings, and begins playback.
     * @param intent Contains the episode list and the starting index.
     */
    private fun setUpPlayer(intent: PlayerIntent.SetUpPlayer) {
        viewModelScope.launch(dispatcherMain) {
            val controller = mediaController ?: controllerFuture.asDeferred().await().also { mediaController = it }

            val settings = playerSettingsRepo.playerSettings.first()

            // Get the saved progress
            val savedProgress = withContext(dispatcherIo) {
                watchedEpsRepo.getEpisodeProgress(intent.animeId, intent.startIndex)
            }

            // UI Update
            updateState {
                it.copy(
                    animeId = intent.animeId,
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

                controller.repeatMode = Player.REPEAT_MODE_OFF

                controller.setMediaItems(mediaItems, intent.startIndex, savedProgress)
                controller.prepare()
                controller.playWhenReady = settings.autoPlay

                startProgressTracker(controller)
            }
        }
    }

    private fun togglePlayPause(controller: MediaController) =
        if (controller.isPlaying) controller.pause() else controller.play()

    private fun stopPlayer(controller: MediaController) {
        saveCurrentProgress()
        controllerVisibilityJob?.cancel()
        progressJob?.cancel()
        controller.stop()
        controller.clearMediaItems()
        updateState { PlayerState() }
    }

    private fun seekRelative(controller: MediaController, offsetMs: Long) =
        controller.seekTo(controller.currentPosition + offsetMs)

    private fun changeMediaItem(controller: MediaController, offset: Int) {
        val currentIndex = controller.currentMediaItemIndex
        val currentPos = (controller.currentPosition - 5000L).coerceAtLeast(0L)

        viewModelScope.launch(dispatcherIo) {
            val animeId = currentState.animeId
            watchedEpsRepo.upsertWatchedEpisode(animeId, currentIndex, currentPos)
        }

        if (offset > 0) {
            if (controller.hasNextMediaItem()) controller.seekToNextMediaItem()
        } else {
            if (controller.hasPreviousMediaItem()) controller.seekToPreviousMediaItem()
        }
    }

    private fun skipOpening(controller: MediaController) {
        val endMs = (currentState.currentEpisode?.opening?.end?.toLong() ?: 0L) * 1000
        controller.seekTo(endMs)
    }

    // --- Sleep time ---
    private fun setSleepTimer(minutes: Int, seconds: Int) {
        if (minutes > 0) {
            sleepTimerManager.setTimer(minutes, seconds)
        } else {
            sleepTimerManager.cancelTimer()
        }
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

    /**
     * Updates the video quality while maintaining the current playback position.
     * Reconstructs the [MediaItem] list and updates the [MediaController].
     */
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
    /**
     * A polling-based tracker that updates the [playerState] with the current
     * playback position every 500ms. Also handles "Auto-Skip" opening logic.
     */
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

    /**
     * Checks if the current playback position falls within the defined "Opening"
     * timestamps and triggers auto-skip or UI button visibility accordingly.
     */
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

    // Progress saving
    private fun saveCurrentProgress() {
        val controller = mediaController ?: return
        val animeId = currentState.animeId
        val currentIndex = controller.currentMediaItemIndex

        // Current - 5 seconds
        val lastPos = (controller.currentPosition - 5000L).coerceAtLeast(0L)

        viewModelScope.launch(dispatcherIo) {
            watchedEpsRepo.insertTitle(animeId)
            watchedEpsRepo.upsertWatchedEpisode(animeId, currentIndex, lastPos)
        }
    }

    private fun saveSpecificEpisodeProgress(index: Int, position: Long = 0L) {
        val animeId = currentState.animeId

        viewModelScope.launch(dispatcherIo) {
            watchedEpsRepo.insertTitle(animeId)
            watchedEpsRepo.upsertWatchedEpisode(animeId, index, position)
        }
    }

    private fun loadAndSeekProgressForIndex(index: Int) {
        val animeId = currentState.animeId
        val controller = mediaController ?: return

        viewModelScope.launch(dispatcherMain) {
            val progress = withContext(dispatcherIo) {
                watchedEpsRepo.getEpisodeProgress(animeId, index)
            }

            if (progress > 0) {
                if (controller.currentMediaItemIndex == index) {
                    controller.seekTo(index, progress)
                }
            }
        }
    }

    // --- Listeners ---
    /**
     * Implementation of [Player.Listener] that syncs the [MediaController]
     * events back into the [PlayerState].
     * * This ensures that when the user interacts with the player (e.g., skips an episode)
     * or the media finishes buffering, the UI state reflects these changes immediately.
     */
    private val playerListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val oldIndex = currentState.currentEpisodeIndex
            val newIndex = mediaController?.currentMediaItemIndex ?: return

            if (oldIndex != newIndex) {
                saveSpecificEpisodeProgress(currentState.animeId)
                loadAndSeekProgressForIndex(newIndex)
            }

            mediaController?.let { ctrl ->
                updateState { it.copy(currentEpisodeIndex = ctrl.currentMediaItemIndex) }
            }
        }

        override fun onPlaybackStateChanged(state: Int) {
            val controller = mediaController ?: return

            if (state == Player.STATE_ENDED) {
                saveSpecificEpisodeProgress(controller.currentMediaItemIndex)
            }

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
        saveCurrentProgress()
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
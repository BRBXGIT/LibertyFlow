package com.example.player.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.VideoQuality
import com.example.data.models.releases.anime_details.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val START_POSITION = 0L

@HiltViewModel
class PlayerVM @Inject constructor(
    val player: ExoPlayer,
    private val playerSettingsRepo: PlayerSettingsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
    @param:Dispatcher(LibertyFlowDispatcher.Main) private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    // --- State ---
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.toLazily(PlayerState())

    // Coroutine jobs management to prevent leaks and overlaps
    private var progressJob: Job? = null
    private var settingsJob: Job? = null
    private var controllerJob: Job? = null

    init {
        setupPlayerListeners()
        observeSettings()
    }

    // --- Intents ---
    fun sendIntent(intent: PlayerIntent) {
        when(intent) {
            // Player Lifecycle & Controls
            is PlayerIntent.SetUpPlayer -> setUpPlayer(intent.episodes, intent.startIndex)
            is PlayerIntent.SetIsScrubbing -> _playerState.update { it.setIsScrubbing(intent.value) }
            PlayerIntent.TogglePlayPause -> togglePlayPause()
            PlayerIntent.StopPlayer -> stopPlayer()

            // Settings & Preferences
            is PlayerIntent.SaveQuality -> saveQuality(intent.quality)
            PlayerIntent.ToggleAutoPlay -> toggleAutoPlay()
            PlayerIntent.ToggleAutoSkipOpening -> toggleAutoSkipOpening()
            PlayerIntent.ToggleShowSkipOpeningButton -> toggleShowSkipOpeningButton()

            // UI Visibility Toggles
            PlayerIntent.ToggleControllerVisible -> toggleControllerVisible()
            PlayerIntent.ToggleFullScreen -> toggleFullScreen()
            PlayerIntent.ToggleCropped -> _playerState.update { it.toggleIsCropped() }
            PlayerIntent.ToggleIsLocked -> _playerState.update { it.toggleIsLocked() }
            PlayerIntent.ToggleEpisodesDialog -> _playerState.update { it.toggleEpisodesDialog() }
            PlayerIntent.ToggleSettingsBS -> _playerState.update { it.toggleSettingsBS() }
            PlayerIntent.ToggleQualityBS -> _playerState.update { it.toggleQualityBS() }
        }
    }

    // --- Effects ---
    fun sendEffect(effect: PlayerEffect) {
        when(effect) {
            is PlayerEffect.SeekForFiveSeconds -> seekFiveSeconds(effect.forward)
            is PlayerEffect.SkipEpisode -> skipEpisode(effect.forward)
            is PlayerEffect.ChangeEpisode -> changeEpisode(effect.index)
            is PlayerEffect.SeekTo -> player.seekTo(effect.position)
        }
    }

    // --- Player ---
    internal val playerListener = object : Player.Listener {
        // Sync internal state when ExoPlayer changes tracks (Auto-play next episode)
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            _playerState.update { it.copy(currentEpisodeIndex = player.currentMediaItemIndex) }
        }

        // Map ExoPlayer states (Buffering/Ready) to UI states (Loading/Playing)
        override fun onPlaybackStateChanged(state: Int) {
            val episodeState = when(state) {
                Player.STATE_BUFFERING -> PlayerState.EpisodeState.Loading
                Player.STATE_READY -> if (player.isPlaying) PlayerState.EpisodeState.Playing else PlayerState.EpisodeState.Paused
                else -> PlayerState.EpisodeState.Paused
            }
            _playerState.update { it.copy(episodeState = episodeState) }
        }
    }

    private fun setupPlayerListeners() = player.addListener(playerListener)

    private fun setUpPlayer(episodes: List<Episode>, startIndex: Int) {
        viewModelScope.launch(dispatcherIo) {
            // 1. Fetch initial settings before touching the player
            val settings = playerSettingsRepo.playerSettings.first()

            // 2. Initialize UI state
            _playerState.update {
                it.copy(
                    uiPlayerState = PlayerState.UiPlayerState.Mini,
                    episodes = episodes,
                    currentEpisodeIndex = startIndex,
                    playerSettings = settings
                )
            }

            // 3. Prepare the Player on the Main Thread
            withContext(dispatcherMain) {
                player.setMediaItems(
                    episodes.map { it.toMediaItem(settings.quality) },
                    startIndex,
                    START_POSITION
                )
                player.prepare()
                player.playWhenReady = settings.autoPlay // Starts immediately if AutoPlay is ON
                startTrackingProgress()
            }
        }
    }

    // --- Progress tracking ---
    /**
     * Runs a loop every 500ms to update the UI progress bar.
     * Also handles "Auto Skip Opening" logic reactively.
     */
    private fun startTrackingProgress() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch(dispatcherMain) {
            while (isActive) {
                val currentPos = player.currentPosition
                val duration = player.duration.coerceAtLeast(0L)
                val state = _playerState.value
                val settings = state.playerSettings

                // 1. Update UI Time
                _playerState.update { it.updateDuration(currentPos, duration) }

                // 2. Check Opening Logic (Auto-Skip or Show Button)
                state.currentEpisode?.opening?.let { opening ->
                    // Convert seconds (API) to milliseconds (ExoPlayer)
                    val start = (opening.start ?: -1).toLong() * 1000L
                    val end = (opening.end ?: -1).toLong() * 1000L

                    // Check if we are currently inside the opening window
                    if (currentPos in start..end) {
                        if (settings.autoSkipOpening) {
                            // OPTION A: Automatically jump over the opening
                            player.seekTo(end)
                        } else {
                            // OPTION B: Show the "Skip" button to the user
                            _playerState.update { it.copy(isSkipOpeningButtonVisible = settings.showSkipOpeningButton) }
                        }
                    } else if (state.isSkipOpeningButtonVisible) {
                        // We left the opening window, hide the button
                        _playerState.update { it.copy(isSkipOpeningButtonVisible = false) }
                    }
                }
                delay(500)
            }
        }
    }

    private fun stopTrackingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    // --- Settings observer ---
    private fun observeSettings() {
        settingsJob?.cancel()
        settingsJob = viewModelScope.launch(dispatcherIo) {
            playerSettingsRepo.playerSettings.collect { newSettings ->
                val oldSettings = _playerState.value.playerSettings

                // Update local state
                _playerState.update { it.copy(playerSettings = newSettings) }

                // React to Quality Change (requires reloading media)
                if (oldSettings.quality != newSettings.quality) {
                    updatePlayerQuality(newSettings.quality)
                }
            }
        }
    }

    /**
     * Hot-swaps the media items to the new quality without resetting position.
     */
    private suspend fun updatePlayerQuality(quality: VideoQuality) = withContext(dispatcherMain) {
        val currentPos = player.currentPosition
        val currentIndex = player.currentMediaItemIndex
        val wasPlaying = player.isPlaying

        // Re-map episodes to the new quality URLs
        val items = _playerState.value.episodes.map { it.toMediaItem(quality) }

        player.setMediaItems(items, currentIndex, currentPos)
        player.prepare()

        // Restore playback state
        if (wasPlaying) player.play()
    }

    // --- Controller ---
    private fun changeEpisode(index: Int) = player.seekTo(index, START_POSITION)

    private fun togglePlayPause() =
        when(player.isPlaying) {
            true -> player.pause()
            false -> player.play()
        }

    private fun skipEpisode(forward: Boolean) =
        if (forward) player.seekToNextMediaItem() else player.seekToPreviousMediaItem()

    private fun seekFiveSeconds(forward: Boolean) {
        val offset = if (forward) 5000L else -5000L
        player.seekTo(player.currentPosition + offset)
    }

    private fun stopPlayer() {
        stopTrackingProgress()
        player.stop()
        player.clearMediaItems()
        _playerState.value = PlayerState()
    }

    // --- Repository updates (Data persistence) ---
    private fun saveQuality(q: VideoQuality) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveQuality(q)
    }

    private fun toggleAutoPlay() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoPlay(!_playerState.value.playerSettings.autoPlay)
    }

    private fun toggleAutoSkipOpening() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoSkipOpening(!_playerState.value.playerSettings.autoSkipOpening)
    }

    private fun toggleShowSkipOpeningButton() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveShowSkipOpeningButton(!_playerState.value.playerSettings.showSkipOpeningButton)
    }

    // --- Ui visibility helpers ---
    private fun toggleControllerVisible() {
        controllerJob?.cancel()
        val isNowVisible = !_playerState.value.isControllerVisible
        _playerState.update { it.setControllerVisible(isNowVisible) }

        // Auto-hide controller after 4 seconds if not locked
        if (isNowVisible && !_playerState.value.isLocked) {
            controllerJob = viewModelScope.launch {
                delay(4000)
                _playerState.update { it.setControllerVisible(false) }
            }
        }
    }

    private fun toggleFullScreen() {
        _playerState.update {
            val next = if (it.uiPlayerState == PlayerState.UiPlayerState.Full)
                PlayerState.UiPlayerState.Mini else PlayerState.UiPlayerState.Full
            it.copy(uiPlayerState = next)
        }
    }
}

// --- Mappers ---
private fun Episode.toMediaItem(quality: VideoQuality) =
    MediaItem.fromUri(
        when(quality) {
            VideoQuality.SD -> hls480
            VideoQuality.HD -> hls720 ?: hls480
            VideoQuality.FHD -> hls1080 ?: hls720 ?: hls480
        }
    )
package com.example.player.player

import androidx.annotation.VisibleForTesting
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
import com.example.data.models.releases.anime_details.UiEpisode
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

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.toLazily(PlayerState())

    // --- Intents ---
    fun sendIntent(intent: PlayerIntent) {
        when(intent) {
            // --- Player ---
            is PlayerIntent.SetUpPlayer -> setUpPlayer(intent.episodes, intent.startIndex)
            is PlayerIntent.SetIsScrubbing -> _playerState.update { it.setIsScrabbing(intent.value) }
            PlayerIntent.TogglePlayPause -> togglePlayPause()
            PlayerIntent.StopPlayer -> stopPlayer()

            // --- Player settings ---
            is PlayerIntent.SaveQuality -> saveQuality(intent.quality)
            PlayerIntent.ToggleAutoPlay -> toggleAutoPlay()
            PlayerIntent.ToggleAutoSkipOpening -> toggleAutoSkipOpening()
            PlayerIntent.ToggleShowSkipOpeningButton -> toggleShowSkipOpeningButton()

            // --- Controller ---
            PlayerIntent.ToggleControllerVisible -> toggleControllerVisible()

            // --- Ui ---
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
            // --- Player ---
            is PlayerEffect.SeekForFiveSeconds -> seekEpisodeForFiveSeconds(effect.forward)
            is PlayerEffect.SkipEpisode -> skipEpisode(effect.forward)
            is PlayerEffect.ChangeEpisode -> changeEpisode(effect.index)
            is PlayerEffect.SeekTo -> seekEpisode(effect.position)
        }
    }

    // --- Player ---
    @VisibleForTesting
    internal val listener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val currentIndex = player.currentMediaItemIndex
            val previousIndex = playerState.value.currentEpisodeIndex

            when {
                currentIndex > previousIndex -> { _playerState.update { it.nextEpisode() } }
                currentIndex < previousIndex -> { _playerState.update { it.previousEpisode() } }
            }
        }
    }

    private fun setUpPlayer(episodes: List<UiEpisode>, startIndex: Int) {
        viewModelScope.launch(dispatcherIo) {
            val initialSettings = playerSettingsRepo.playerSettings.first()

            _playerState.update {
                it.copy(
                    uiPlayerState = PlayerState.UiPlayerState.Mini,
                    episodes = episodes,
                    currentEpisodeIndex = startIndex,
                    playerSettings = initialSettings
                )
            }

            withContext(dispatcherMain) {
                player.clearMediaItems()
                player.addListener(listener)

                val mediaItems = episodes.map { it.toMediaItem(initialSettings.quality) }
                player.setMediaItems(mediaItems, startIndex, START_POSITION)

                player.prepare()
                player.playWhenReady = true
                startTrackingProgress()
            }
        }

        observeSettings()
    }

    private fun stopPlayer() {
        player.clearMediaItems()
        stopTrackingProgress()
        player.stop()

        _playerState.value = PlayerState()
    }

    private fun skipEpisode(forward: Boolean) {
        when(forward) {
            true -> if (player.hasNextMediaItem()) player.seekToNextMediaItem()
            false -> if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem()
        }
    }

    private fun togglePlayPause() {
        when(player.isPlaying) {
            true -> {
                player.pause()
                _playerState.update { it.setEpisodeState(PlayerState.EpisodeState.Paused) }
            }
            false -> {
                player.play()
                _playerState.update { it.setEpisodeState(PlayerState.EpisodeState.Playing) }
            }
        }
    }

    private fun seekEpisodeForFiveSeconds(forward: Boolean) =
        when(forward) {
            true -> player.seekForward()
            false -> player.seekBack()
        }

    private fun seekEpisode(position: Long) = player.seekTo(position)

    private fun changeEpisode(index: Int) = player.seekTo(index, START_POSITION)

    private var progressJob: Job? = null
    private fun startTrackingProgress() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch(dispatcherMain) {
            while (isActive) {
                val current = player.currentPosition
                val duration = player.duration.coerceAtLeast(0L)

                _playerState.update { it.updateDuration(current, duration) }

                delay(500)
            }
        }
    }

    private fun stopTrackingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    // --- Player settings ---
    private var settingsJob: Job? = null

    private fun observeSettings() {
        settingsJob?.cancel()
        settingsJob = viewModelScope.launch(dispatcherIo) {
            playerSettingsRepo.playerSettings.collect { newSettings ->
                val oldSettings = _playerState.value.playerSettings

                _playerState.update { it.copy(playerSettings = newSettings) }

                if (oldSettings.quality != newSettings.quality) {
                    updatePlayerQuality(newSettings.quality)
                }
            }
        }
    }

    private suspend fun updatePlayerQuality(newQuality: VideoQuality) {
        withContext(dispatcherMain) {
            val currentPos = player.currentPosition
            val currentIndex = player.currentMediaItemIndex
            val isPlaying = player.isPlaying

            val newItems = _playerState.value.episodes.map { it.toMediaItem(newQuality) }

            player.setMediaItems(newItems, currentIndex, currentPos)

            player.prepare()
            if (isPlaying) player.play()
        }
    }

    private fun saveQuality(quality: VideoQuality) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveQuality(quality)
    }

    private fun toggleShowSkipOpeningButton() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveShowSkipOpeningButton(!_playerState.value.playerSettings.showSkipOpeningButton)
    }

    private fun toggleAutoSkipOpening() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoSkipOpening(!_playerState.value.playerSettings.autoSkipOpening)
    }

    private fun toggleAutoPlay() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoPlay(!_playerState.value.playerSettings.autoPlay)
    }

    // --- Controller ---
    private var controllerJob: Job? = null
    private fun toggleControllerVisible() {
        controllerJob?.cancel()

        if (_playerState.value.isControllerVisible) {
            _playerState.update { it.setControllerVisible(false) }
        } else {
            controllerJob = viewModelScope.launch {
                _playerState.update { it.setControllerVisible(true) }
                delay(4000)
                _playerState.update { it.setControllerVisible(false) }
            }
        }
    }

    // --- Ui ---
    private fun toggleFullScreen() {
        _playerState.update {
            val newState = if (it.uiPlayerState == PlayerState.UiPlayerState.Full)
                PlayerState.UiPlayerState.Mini else PlayerState.UiPlayerState.Full
            it.setUiPlayerState(newState)
        }
    }
}

private fun UiEpisode.toMediaItem(quality: VideoQuality) =
    MediaItem.fromUri(
        when(quality) {
            VideoQuality.SD -> hls480
            VideoQuality.HD -> hls720 ?: hls480
            VideoQuality.FHD -> hls1080 ?: hls720 ?: hls480
        }
    )
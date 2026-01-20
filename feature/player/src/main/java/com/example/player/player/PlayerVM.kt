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
import com.example.data.models.releases.anime_details.UiEpisode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val START_POSITION = 0L

@HiltViewModel
class PlayerVM @Inject constructor(
    val player: ExoPlayer,
    @param:Dispatcher(LibertyFlowDispatcher.Main) private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.toLazily(PlayerState())

    private val _playerEffects = Channel<PlayerEffect>(Channel.BUFFERED)
    val playerEffects = _playerEffects.receiveAsFlow()

    // --- Intents ---
    fun sendIntent(intent: PlayerIntent) {
        // TODO: handle intent
    }

    // --- Effects ---
    fun sendEffect(effect: PlayerEffect) {
        when(effect) {
            // --- Player ---
            is PlayerEffect.SetUpPlayer -> setUpPlayer(effect.episodes, effect.startIndex)
            is PlayerEffect.SeekForFiveSeconds -> seekEpisodeForFiveSeconds(effect.forward)
            is PlayerEffect.SkipEpisode -> skipEpisode(effect.forward)
            is PlayerEffect.ChangeEpisode -> changeEpisode(effect.index)
            is PlayerEffect.SeekTo -> seekEpisode(effect.position)
            PlayerEffect.TogglePlayPause -> playPauseEpisode()
            PlayerEffect.StopPlayer -> stopPlayer()
            PlayerEffect.ToggleIsScrubbing -> _playerState.update { it.toggleIsScrubbing() }

            // --- Controller ---
            PlayerEffect.ToggleControllerVisible -> toggleControllerVisible()

            // --- Ui ---
            PlayerEffect.ToggleFullScreen -> toggleFullScreen()
            PlayerEffect.ToggleCropped -> _playerState.update { it.toggleIsCropped() }
            PlayerEffect.ToggleIsLocked -> _playerState.update { it.toggleIsLocked() }
            PlayerEffect.ToggleEpisodesDialog -> _playerState.update { it.toggleEpisodesDialog() }
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
        _playerState.update {
            it.copy(
                uiPlayerState = PlayerState.UiPlayerState.Mini,
                episodes = episodes,
                currentEpisodeIndex = startIndex
            )
        }

        player.clearMediaItems()
        player.addListener(listener)

        val quality = _playerState.value.videoQuality
        val mediaItems = episodes.map { it.toMediaItem(quality) }
        player.setMediaItems(mediaItems, startIndex, START_POSITION)

        player.prepare()
        player.playWhenReady = true
        startTrackingProgress()
    }

    private fun stopPlayer() {
        player.clearMediaItems()
        stopTrackingProgress()
        player.stop()

        _playerState.value = PlayerState()
    }

    private fun skipEpisode(forward: Boolean) {
        when(forward) {
            true -> {
                if (player.hasNextMediaItem()) player.seekToNextMediaItem()
            }
            false -> {
                if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem()
            }
        }
    }

    private fun playPauseEpisode() {
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

private fun UiEpisode.toMediaItem(quality: PlayerState.VideoQuality) =
    MediaItem.fromUri(
        when(quality) {
            PlayerState.VideoQuality.SD -> hls480
            PlayerState.VideoQuality.HD -> hls720 ?: hls480
            PlayerState.VideoQuality.FHD -> hls1080 ?: hls720 ?: hls480
        }
    )
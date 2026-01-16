package com.example.player.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
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
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            PlayerEffect.TogglePlayPause -> playPauseEpisode()
            PlayerEffect.StopPlayer -> stopPlayer()

            // --- Controller ---
            PlayerEffect.ToggleControllerVisible -> toggleControllerVisible()

            // --- Ui ---
            PlayerEffect.ToggleFullScreen -> toggleFullScreen()
        }
    }

    // --- Player ---
    private fun setUpPlayer(episodes: List<UiEpisode>, startIndex: Int) {
        _playerState.update { it.setPlayerState(PlayerState.UiPlayerState.Mini) }

        player.clearMediaItems()

        val quality = _playerState.value.videoQuality
        val mediaItems = episodes.map { it.toMediaItem(quality) }
        player.setMediaItems(mediaItems, startIndex, 0L)

        player.prepare()
        player.playWhenReady = true
    }

    private fun stopPlayer() {
        player.clearMediaItems()
        player.stop()

        _playerState.update { it.setPlayerState(PlayerState.UiPlayerState.Closed) }
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
    fun toggleFullScreen() {
        _playerState.update {
            val newState = if (it.uiPlayerState == PlayerState.UiPlayerState.Full)
                PlayerState.UiPlayerState.Mini else PlayerState.UiPlayerState.Full
            it.setPlayerState(newState)
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
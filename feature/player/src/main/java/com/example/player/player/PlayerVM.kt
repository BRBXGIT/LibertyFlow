package com.example.player.player

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.data.models.releases.anime_details.UiEpisode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    fun sendIntent(intent: PlayerIntent) {
        // TODO: handle intent
    }

    fun sendEffect(effect: PlayerEffect) {
        when(effect) {
            is PlayerEffect.SetUpPlayer -> setUpPlayer(effect.episodes, effect.startIndex)
        }
    }

    private fun setUpPlayer(episodes: List<UiEpisode>, startIndex: Int) {
        player.clearMediaItems()

        val quality = _playerState.value.videoQuality
        val mediaItems = episodes.map { it.toMediaItem(quality) }
        player.setMediaItems(mediaItems, startIndex, 0L)

        player.prepare()
        player.playWhenReady = true
    }
}

private fun UiEpisode.toMediaItem(quality: PlayerState.VideoQuality) =
    MediaItem.fromUri(
        when(quality) {
            PlayerState.VideoQuality.SD -> hls480
            PlayerState.VideoQuality.HD -> hls720
            PlayerState.VideoQuality.FHD -> hls1080 ?: hls720
        }
    )
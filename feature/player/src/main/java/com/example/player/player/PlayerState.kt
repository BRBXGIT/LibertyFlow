package com.example.player.player

import androidx.compose.runtime.Immutable
import com.example.data.models.releases.anime_details.UiEpisode

@Immutable
data class PlayerState(
    // Player states
    val playerState: PlayerState = PlayerState.Closed,
    val episodes: List<UiEpisode> = emptyList(),
    val videoQuality: VideoQuality = VideoQuality.SD,

    // Episode states
    val episodeState: EpisodeState = EpisodeState.Loading,
    val episodeTime: EpisodeTime = EpisodeTime(),
    val isControllerVisible: Boolean = false,
) {
    enum class EpisodeState { Loading, Playing, Paused }

    enum class VideoQuality { SD, HD, FHD }

    enum class PlayerState { Closed, Mini, Full }

    @Immutable
    data class EpisodeTime(
        val current: Long = 0L,
        val total: Long = 0L,
    )

    // Sets
    fun setPlayerState(value: PlayerState) = copy(playerState = value)

    fun setEpisodeState(value: EpisodeState) = copy(episodeState = value)

    fun setControllerVisible(value: Boolean) = copy(isControllerVisible = value)
}
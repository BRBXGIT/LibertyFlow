package com.example.player.player

import androidx.compose.runtime.Immutable
import com.example.data.models.releases.anime_details.UiEpisode

@Immutable
data class PlayerState(
    // Player states
    val uiPlayerState: UiPlayerState = UiPlayerState.Closed,
    val episodes: List<UiEpisode> = emptyList(),
    val videoQuality: VideoQuality = VideoQuality.SD,
    val currentEpisodeIndex: Int = 0,

    // Episode states
    val episodeState: EpisodeState = EpisodeState.Loading,
    val episodeTime: EpisodeTime = EpisodeTime(),

    // --- Controller ---
    val isControllerVisible: Boolean = false,

    // --- Ui ---
    val isCropped: Boolean = false,
    val isLocked: Boolean = false
) {
    enum class EpisodeState { Loading, Playing, Paused }

    enum class VideoQuality { SD, HD, FHD }

    enum class UiPlayerState { Closed, Mini, Full }

    @Immutable
    data class EpisodeTime(
        val current: Long = 0L,
        val total: Long = 0L,
    )

    // Sets
    fun setUiPlayerState(value: UiPlayerState) = copy(uiPlayerState = value)

    fun setEpisodeState(value: EpisodeState) = copy(episodeState = value)

    fun setControllerVisible(value: Boolean) = copy(isControllerVisible = value)

    // Toggles
    fun toggleIsCropped() = copy(isCropped = !isCropped)

    fun toggleIsLocked() = copy(isLocked = !isLocked, isControllerVisible = false)
}
package com.example.player.player

import androidx.compose.runtime.Immutable
import com.example.data.models.player.PlayerSettings
import com.example.data.models.releases.anime_details.Episode

@Immutable
data class PlayerState(
    // Content & Settings
    val uiPlayerState: UiPlayerState = UiPlayerState.Closed,
    val episodes: List<Episode> = emptyList(),
    val currentEpisodeIndex: Int = 0,
    val playerSettings: PlayerSettings = PlayerSettings(),

    // Playback Progress
    val episodeState: EpisodeState = EpisodeState.Loading,
    val episodeTime: EpisodeTime = EpisodeTime(),

    // UI Overlays
    val isControllerVisible: Boolean = false,
    val isSkipOpeningButtonVisible: Boolean = false,
    val isEpisodesDialogVisible: Boolean = false,
    val isSettingsBSVisible: Boolean = false,
    val isQualityBSVisible: Boolean = false,

    // View Modifiers
    val isCropped: Boolean = false,
    val isLocked: Boolean = false
) {
    /** Accessor for the active episode */
    val currentEpisode: Episode? get() = episodes.getOrNull(currentEpisodeIndex)

    enum class EpisodeState { Loading, Playing, Paused }
    enum class UiPlayerState { Closed, Mini, Full }

    @Immutable
    data class EpisodeTime(
        val current: Long = 0L,
        val total: Long = 0L,
        val isScrubbing: Boolean = false
    )

    // --- State Mappers ---
    fun toggleIsCropped() = copy(isCropped = !isCropped)
    fun toggleIsLocked() = copy(
        isLocked = !isLocked,
        isControllerVisible = false
    )

    fun toggleEpisodesDialog() = copy(isEpisodesDialogVisible = !isEpisodesDialogVisible)
    fun toggleSettingsBS() = copy(isSettingsBSVisible = !isSettingsBSVisible)
    fun toggleQualityBS() = copy(isQualityBSVisible = !isQualityBSVisible)

    fun setControllerVisible(value: Boolean) = copy(isControllerVisible = value)
    fun setIsScrubbing(value: Boolean) = copy(episodeTime = episodeTime.copy(isScrubbing = value))
    fun updateDuration(current: Long, total: Long) = copy(episodeTime = episodeTime.copy(current = current, total = total))
}
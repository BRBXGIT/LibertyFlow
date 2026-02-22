package com.example.player.player

import androidx.compose.runtime.Immutable
import com.example.data.models.player.PlayerSettings
import com.example.data.models.releases.anime_details.Episode

/**
 * Represents the entire UI and playback state for the video player.
 * * This class is [Immutable], ensuring that every state change results in a new instance,
 * which is a requirement for efficient recomposition in Jetpack Compose.
 *
 * @property animeName The title of the anime currently being played.
 * @property uiPlayerState The visual scale/mode of the player (Closed, Mini, or Full).
 * @property episodes The list of available episodes for the current series.
 * @property currentEpisodeIndex The index of the episode currently loaded in the player.
 * @property playerSettings User-defined preferences like quality and auto-skip.
 * @property episodeState The current playback status (Loading, Playing, or Paused).
 * @property episodeTime Precise timing information, including current position and total duration.
 * @property isControllerVisible Whether the playback controls overlay is currently shown.
 * @property isLocked When true, interaction with the UI controls is disabled to prevent accidental touches.
 * @property currentSleepTime Current sleep time if sleep mode i enabled.
 */
@Immutable
data class PlayerState(
    // Content & Settings
    val animeName: String = "",
    val animeId: Int = 0,
    val uiPlayerState: UiPlayerState = UiPlayerState.Closed,
    val episodes: List<Episode> = emptyList(),
    val currentEpisodeIndex: Int = 0,
    val playerSettings: PlayerSettings = PlayerSettings(),

    // Playback Progress
    val episodeState: EpisodeState = EpisodeState.Loading,
    val episodeTime: EpisodeTime = EpisodeTime(),
    val currentSleepTime: Int? = null,

    // UI Overlays
    val isControllerVisible: Boolean = false,
    val isSkipOpeningButtonVisible: Boolean = false,
    val isEpisodesDialogVisible: Boolean = false,
    val isSettingsDialogVisible: Boolean = false,
    val isQualityDialogVisible: Boolean = false,
    val isSleepDialogVisible: Boolean = false,

    // View Modifiers
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
    fun toggleIsLocked() = copy(
        isLocked = !isLocked,
        isControllerVisible = false
    )

    fun toggleEpisodesDialog() = copy(isEpisodesDialogVisible = !isEpisodesDialogVisible)
    fun toggleSettingsBS() = copy(isSettingsDialogVisible = !isSettingsDialogVisible)
    fun toggleQualityBS() = copy(isQualityDialogVisible = !isQualityDialogVisible)
    fun toggleSleepDialog() = copy(isSleepDialogVisible = !isSleepDialogVisible)

    fun setControllerVisible(value: Boolean) = copy(isControllerVisible = value)
    fun setIsScrubbing(value: Boolean) = copy(episodeTime = episodeTime.copy(isScrubbing = value))
    fun updateDuration(current: Long, total: Long) = copy(episodeTime = episodeTime.copy(current = current, total = total))
}
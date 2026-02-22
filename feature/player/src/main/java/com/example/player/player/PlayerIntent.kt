package com.example.player.player

import com.example.data.models.player.VideoQuality
import com.example.data.models.releases.anime_details.Episode

/**
 * Represents all possible user actions or system events that can modify the [PlayerState]
 * or control the MediaController.
 * * Intents are processed by the [PlayerVM.sendIntent] method, ensuring that the UI
 * remains a "passive" observer of the state.
 */
sealed interface PlayerIntent {
    // --- Player ---
    data class SetUpPlayer(
        val episodes: List<Episode>,
        val startIndex: Int,
        val animeName: String,
        val animeId: Int
    ): PlayerIntent
    data class SetIsScrubbing(val value: Boolean): PlayerIntent
    data class SeekForFiveSeconds(val forward: Boolean): PlayerIntent
    data class SkipEpisode(val forward: Boolean): PlayerIntent
    data class ChangeEpisode(val index: Int): PlayerIntent
    data class SeekTo(val position: Long): PlayerIntent
    data object TogglePlayPause: PlayerIntent
    data object StopPlayer: PlayerIntent
    data object SkipOpening: PlayerIntent


    // --- Sleep timer ---
    data class SetSleepTimer(val minutes: Int, val seconds: Int): PlayerIntent


    // --- Controller ---
    data object ToggleControllerVisible: PlayerIntent
    data object TurnOffController: PlayerIntent


    // --- Player settings ---
    data class SaveQuality(val quality: VideoQuality): PlayerIntent
    data object ToggleShowSkipOpeningButton: PlayerIntent
    data object ToggleAutoSkipOpening: PlayerIntent
    data object ToggleAutoPlay: PlayerIntent


    // --- Ui ---
    data object ToggleFullScreen: PlayerIntent
    data object ToggleCropped: PlayerIntent
    data object ToggleIsLocked: PlayerIntent
    data object ToggleEpisodesDialog: PlayerIntent
    data object ToggleSettingsDialog: PlayerIntent
    data object ToggleQualityDialog: PlayerIntent
}
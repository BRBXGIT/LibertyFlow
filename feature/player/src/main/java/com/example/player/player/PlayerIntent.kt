package com.example.player.player

import com.example.data.models.player.VideoQuality
import com.example.data.models.releases.anime_details.UiEpisode

sealed interface PlayerIntent {
    // --- Player ---
    data class SetUpPlayer(val episodes: List<UiEpisode>, val startIndex: Int): PlayerIntent
    data class SetIsScrubbing(val value: Boolean): PlayerIntent
    data object TogglePlayPause: PlayerIntent
    data object StopPlayer: PlayerIntent

    // --- Controller ---
    data object ToggleControllerVisible: PlayerIntent

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
    data object ToggleSettingsBS: PlayerIntent
}
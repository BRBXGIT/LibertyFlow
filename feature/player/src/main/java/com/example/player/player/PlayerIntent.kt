package com.example.player.player

import com.example.data.models.player.VideoQuality

sealed interface PlayerIntent {
    // --- Player settings ---
    data class SaveQuality(val quality: VideoQuality): PlayerIntent
    data object ToggleShowSkipOpeningButton: PlayerIntent
    data object ToggleAutoSkipOpening: PlayerIntent
    data object ToggleAutoPlay: PlayerIntent
}
package com.example.data.models.player

import androidx.compose.runtime.Immutable

@Immutable
data class PlayerSettings(
    val quality: VideoQuality = VideoQuality.SD,
    val showSkipOpeningButton: Boolean = false,
    val autoSkipOpening: Boolean= false,
    val autoPlay: Boolean = false
)

package com.example.data.models.player

import androidx.compose.runtime.Immutable

/**
 * Persistent configuration for the video player experience.
 *
 * This class is marked as @Immutable to allow for efficient UI updates in
 * the player overlay. When a single setting changes, the Compose compiler
 * can easily determine if specific UI components need to be redrawn.
 *
 * @property quality The preferred video resolution (e.g., SD, HD, FHD). Defaults to [VideoQuality.SD].
 * @property showSkipOpeningButton Whether to display a manual "Skip Opening" button when a marker is hit.
 * @property autoSkipOpening Whether the player should automatically jump past openings without user input.
 * @property autoPlay Whether the next episode should start automatically after the current one ends.
 * @property isCropped Whether the video should be cropped/zoomed to fill the aspect ratio of the screen.
 */
@Immutable
data class PlayerSettings(
    val quality: VideoQuality = VideoQuality.SD,
    val showSkipOpeningButton: Boolean = false,
    val autoSkipOpening: Boolean= false,
    val autoPlay: Boolean = false,
    val isCropped: Boolean = false
)

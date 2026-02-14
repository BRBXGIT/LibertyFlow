package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable

/**
 * Represents a specific episode of an anime, including media streams and playback markers.
 * * This class is marked with [@Immutable] to support high-performance rendering in
 * episode selection lists and player quality-selectors.
 *
 * @property opening Timestamp markers for the episode's opening sequence.
 * @property ending Timestamp markers for the episode's ending sequence.
 * @property hls1080 The HLS stream URL for 1080p (Full HD) resolution. Null if unavailable.
 * @property hls480 The HLS stream URL for 480p (SD) resolution. This is the baseline quality.
 * @property hls720 The HLS stream URL for 720p (HD) resolution. Null if unavailable.
 * @property name The specific title of the episode (e.g., "The Beginning"). Null if only the number is known.
 */
@Immutable
data class Episode(
    val opening: Opening,
    val ending: Ending,
    val hls1080: String?,
    val hls480: String,
    val hls720: String?,
    val name: String?,
)
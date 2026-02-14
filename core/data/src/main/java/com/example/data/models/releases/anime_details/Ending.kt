package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable

/**
 * Defines the timestamp markers for an anime's ending sequence (ED).
 *
 * This data is used by the player to identify when the credits begin and end,
 * facilitating features like the "Skip Ending" button or automatic transition
 * to the next episode.
 *
 * @property start The timestamp (in seconds) where the ending sequence begins.
 * Returns null if no marker is available.
 * @property end The timestamp (in seconds) where the ending sequence concludes.
 * Returns null if no marker is available.
 */
@Immutable
data class Ending(
    val start: Int?,
    val end: Int?
)

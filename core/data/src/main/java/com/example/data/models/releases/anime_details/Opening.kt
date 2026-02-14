package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable

/**
 * Defines the timestamp markers for an anime's opening sequence (OP).
 *
 * Used by the player to identify the intro theme duration, enabling
 * "Skip Opening" functionality.
 *
 * @property start The timestamp (in seconds) where the opening begins.
 * Returns null if no marker is provided by the API.
 * @property end The timestamp (in seconds) where the opening concludes.
 * Returns null if no marker is provided by the API.
 */
@Immutable
data class Opening(
    val start: Int?,
    val end: Int?
)

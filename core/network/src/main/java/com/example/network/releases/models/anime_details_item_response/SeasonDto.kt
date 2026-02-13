package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the release season of an anime.
 *
 * @property description The localized name of the season (e.g., 'Winter', 'Spring').
 */
data class SeasonDto(
    @field:SerializedName(FIELD_DESCRIPTION)
    val description: String
) {
    companion object Fields {
        private const val FIELD_DESCRIPTION = "description"
    }
}
package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the format of the anime release.
 * @property description The localized format type (e.g., "TV", "Movie", "OVA", "ONA").
 */
data class TypeDto(
    @field:SerializedName(FIELD_DESCRIPTION)
    val description: String
) {
    companion object Fields {
        private const val FIELD_DESCRIPTION = "description"
    }
}
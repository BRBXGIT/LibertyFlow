package com.example.network.favorites.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a single favorite entry.
 *
 * @property releaseId The unique identifier of the favorited anime release.
 */
data class FavoriteAnimeIdItemDto(
    @field:SerializedName(FIELD_RELEASE_ID)
    val releaseId: Int
) {
    companion object Fields {
        private const val FIELD_RELEASE_ID = "release_id"
    }
}

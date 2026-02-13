package com.example.network.releases.models.anime_id_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a minimal anime reference.
 * * Used only for random anime endpoint.
 * to keep the network payload as small as possible.
 *
 * @property id The unique identifier of the anime release.
 */
data class AnimeIdItemDto(
    @field:SerializedName(FIELD_ID)
    val id: Int
) {
    companion object Fields {
        private const val FIELD_ID = "id"
    }
}
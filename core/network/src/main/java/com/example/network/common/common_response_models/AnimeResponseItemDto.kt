package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a single anime entry in the catalog response.
 *
 * @property id Unique identifier for the anime release.
 * @property genres List of associated genres (e.g., Action, Seinen).
 * @property nameDto Structured object containing various language versions of the title.
 * @property posterDto Object containing image URLs for the anime's poster.
 */
data class AnimeResponseItemDto(
    @field:SerializedName(FIELD_ID)
    val id: Int,

    @field:SerializedName(FIELD_GENRES)
    val genres: List<GenreDto>,

    @field:SerializedName(FIELD_NAME)
    val nameDto: NameDto,

    @field:SerializedName(FIELD_POSTER)
    val posterDto: PosterDto
) {
    companion object Fields {
        private const val FIELD_ID = "id"
        private const val FIELD_GENRES = "genres"
        private const val FIELD_NAME = "name"
        private const val FIELD_POSTER = "poster"
    }
}
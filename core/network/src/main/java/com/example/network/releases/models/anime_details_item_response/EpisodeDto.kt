package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing an individual anime episode and its streaming sources.
 *
 * @property endingDto Timestamps for the ending sequence.
 * @property hls1080 URL for the 1080p HLS stream.
 * @property hls480 URL for the 480p HLS stream (Standard Definition fallback).
 * @property hls720 URL for the 720p HLS stream.
 * @property name The title of the episode (e.g., "The Journey Begins").
 * @property openingDto Timestamps for the opening sequence.
 */
data class EpisodeDto(
    @field:SerializedName(FIELD_ENDING)
    val endingDto: EndingDto,

    @field:SerializedName(FIELD_HLS_1080)
    val hls1080: String?,

    @field:SerializedName(FIELD_HLS_480)
    val hls480: String, // TODO: Fix bug with all quality null

    @field:SerializedName(FIELD_HLS_720)
    val hls720: String?,

    @field:SerializedName(FIELD_NAME)
    val name: String?,

    @field:SerializedName(FIELD_OPENING)
    val openingDto: OpeningDto
) {
    companion object Fields {
        private const val FIELD_ENDING = "ending"
        private const val FIELD_HLS_1080 = "hls_1080"
        private const val FIELD_HLS_480 = "hls_480"
        private const val FIELD_HLS_720 = "hls_720"
        private const val FIELD_NAME = "name"
        private const val FIELD_OPENING = "opening"
    }
}
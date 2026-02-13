package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the timestamp range for an episode's opening sequence.
 * This data is essential for the "Skip Intro" feature in the video player.
 *
 * @property start The timestamp (in seconds) where the opening credits begin.
 * @property stop The timestamp (in seconds) where the opening credits end.
 */
data class OpeningDto(
    @field:SerializedName(FIELD_START)
    val start: Int?,

    @field:SerializedName(FIELD_STOP)
    val stop: Int?
) {
    companion object Fields {
        private const val FIELD_START = "start"
        private const val FIELD_STOP = "stop"
    }
}
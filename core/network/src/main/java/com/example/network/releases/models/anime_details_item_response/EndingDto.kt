package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the timestamp range for an episode's ending credits.
 * Useful for "Skip Credits" functionality in the video player.
 *
 * @property start The timestamp (in seconds) where the ending theme begins.
 * @property stop The timestamp (in seconds) where the ending theme concludes.
 */
data class EndingDto(
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
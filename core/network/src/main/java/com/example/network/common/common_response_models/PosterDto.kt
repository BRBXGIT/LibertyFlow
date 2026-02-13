package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object containing poster image information for an anime release.
 *
 * @property optimizedDto A structured object containing various optimized versions
 * of the poster (source, preview, and thumbnail).
 */
data class PosterDto(
    @field:SerializedName(FIELD_OPTIMIZED)
    val optimizedDto: OptimizedDto
) {
    companion object Fields {
        private const val FIELD_OPTIMIZED = "optimized"
    }
}
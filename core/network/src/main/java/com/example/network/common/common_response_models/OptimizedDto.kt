package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

/**
 * DTO containing URLs for different image resolutions and optimizations.
 *
 * @property src The primary source URL for the high-resolution image.
 * @property preview A mid-sized image URL suitable for list previews.
 * @property thumbnail A small-sized image URL optimized for fast loading and low bandwidth.
 */
data class OptimizedDto(
    @field:SerializedName(FIELD_SRC)
    val src: String,

    @field:SerializedName(FIELD_PREVIEW)
    val preview: String,

    @field:SerializedName(FIELD_THUMBNAIL)
    val thumbnail: String
) {
    companion object Fields {
        private const val FIELD_SRC = "src"
        private const val FIELD_PREVIEW = "preview"
        private const val FIELD_THUMBNAIL = "thumbnail"
    }
}

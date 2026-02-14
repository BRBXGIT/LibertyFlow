package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

private const val BASE_POSTER_URL = "https://aniliberty.top"

/**
 * Defines the available sizes and formats for anime poster images.
 * @property THUMBNAIL A low-resolution version for lists or small icons.
 * @property PREVIEW A medium-resolution version for detail headers.
 * @property SRC The original, high-resolution source image.
 */
enum class PosterType { THUMBNAIL, PREVIEW, SRC }

/**
 * Data holder for relative poster paths provided by the API.
 * * This class is [@Immutable], making it safe for use in high-performance
 * UI components like image loaders within Compose.
 *
 * @property thumbnail Relative path to the thumbnail image.
 * @property preview Relative path to the preview image.
 * @property src Relative path to the full-size source image.
 */
@Immutable
data class Poster(
    val thumbnail: String,
    val preview: String,
    val src: String
) {
    fun fullPath(posterType: PosterType): String {
        return when(posterType) {
            PosterType.THUMBNAIL -> BASE_POSTER_URL + thumbnail
            PosterType.PREVIEW -> BASE_POSTER_URL + preview
            PosterType.SRC -> BASE_POSTER_URL + src
        }
    }
}

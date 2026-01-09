package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

private const val BASE_POSTER_URL = "https://aniliberty.top"

enum class PosterType { THUMBNAIL, PREVIEW, SRC }

@Immutable
data class UiPoster(
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

package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

private object Utils {
    const val BASE_POSTER_URL = "https://anilibria.top/"
}

enum class PosterType { THUMBNAIL, PREVIEW, SRC }

@Immutable
data class UiPoster(
    val thumbnail: String,
    val preview: String,
    val src: String
) {
    fun fullPath(posterType: PosterType): String {
        return when(posterType) {
            PosterType.THUMBNAIL -> Utils.BASE_POSTER_URL + thumbnail
            PosterType.PREVIEW -> Utils.BASE_POSTER_URL + preview
            PosterType.SRC -> Utils.BASE_POSTER_URL + src
        }
    }
}

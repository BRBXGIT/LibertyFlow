package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

private object Constants {
    const val BASE_POSTER_URL = "https://aniliberty.top/"
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
            PosterType.THUMBNAIL -> Constants.BASE_POSTER_URL + thumbnail
            PosterType.PREVIEW -> Constants.BASE_POSTER_URL + preview
            PosterType.SRC -> Constants.BASE_POSTER_URL + src
        }
    }
}

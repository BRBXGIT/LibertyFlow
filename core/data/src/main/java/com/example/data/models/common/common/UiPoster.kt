package com.example.data.models.common.common

enum class PosterType {
    THUMBNAIL, PREVIEW, SRC
}

data class UiPoster(
    val thumbnail: String,
    val preview: String,
    val src: String
) {
    fun fullPath(posterType: PosterType, basePath: String): String {
        return when(posterType) {
            PosterType.THUMBNAIL -> basePath + thumbnail
            PosterType.PREVIEW -> basePath + preview
            PosterType.SRC -> basePath + src
        }
    }
}

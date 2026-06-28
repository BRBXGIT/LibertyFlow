package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable

@Immutable
data class Poster(
    private val preview: String,
    private val src: String,
    private val thumbnail: String,
) {
    private val basePath = "https://aniliberty.top"

    fun fullPreview(): String = basePath + preview
    fun fullSrc(): String = basePath + src
    fun fullThumbnail(): String = basePath + thumbnail
}

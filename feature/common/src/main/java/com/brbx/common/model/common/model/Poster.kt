package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import com.brbx.common.common.CommonConstants

@Immutable
data class Poster(
    private val preview: String,
    private val src: String,
    private val thumbnail: String,
) {
    fun fullPreview(): String = CommonConstants.BasePosterPath + preview
    fun fullSrc(): String = CommonConstants.BasePosterPath + src
    fun fullThumbnail(): String = CommonConstants.BasePosterPath + thumbnail
}

package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class Poster(
    val preview: String,
    val src: String,
    val thumbnail: String,
) { companion object }

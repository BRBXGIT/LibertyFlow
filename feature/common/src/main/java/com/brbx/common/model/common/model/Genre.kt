package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class Genre(
    val id: Int,
    val name: String,
) { companion object }

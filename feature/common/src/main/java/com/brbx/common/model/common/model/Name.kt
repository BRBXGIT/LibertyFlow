package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class Name(
    val alternative: String?,
    val english: String,
    val russian: String,
) { companion object }

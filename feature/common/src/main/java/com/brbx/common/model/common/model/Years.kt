package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import java.time.LocalDateTime

@Immutable
@optics
data class Years(
    val from: Int = 0,
    val to: Int = LocalDateTime.now().year,
) { companion object }

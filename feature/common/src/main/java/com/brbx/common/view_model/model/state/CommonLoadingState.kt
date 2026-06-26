package com.brbx.common.view_model.model.state

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class CommonLoadingState(
    val isLoading: Boolean = false,
    val isException: Boolean = false,
) { companion object }

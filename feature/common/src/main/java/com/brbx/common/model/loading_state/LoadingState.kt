package com.brbx.common.model.loading_state

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class LoadingState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    fun withLoading(loading: Boolean): LoadingState =
        this.copy(isLoading = loading)

    fun withError(error: Boolean): LoadingState =
        this.copy(isError = error)

    companion object
}

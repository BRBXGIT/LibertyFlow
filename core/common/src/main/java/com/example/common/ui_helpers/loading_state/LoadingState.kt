package com.example.common.ui_helpers.loading_state

import androidx.compose.runtime.Immutable

@Immutable
data class LoadingState(
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    fun withLoading(value: Boolean) = copy(isLoading = value)
    fun withError(value: Boolean) = copy(isError = value)
}

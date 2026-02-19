package com.example.common.vm_helpers.loading

import androidx.compose.runtime.Immutable

/**
 * A lightweight state container for tracking the loading and error status of an operation.
 *
 * This class is typically used in conjunction with other state classes to manage
 * the visibility of progress indicators (shimmers/spinners) and error placeholders.
 *
 * @property isLoading True if an asynchronous operation is currently in progress.
 * @property isError True if the last operation failed and requires user attention.
 */
@Immutable
data class LoadingState(
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    fun withLoading(value: Boolean) = copy(isLoading = value)
    fun withError(value: Boolean) = copy(isError = value)
    fun withBoth(loading: Boolean, error: Boolean) = copy(isLoading = loading, isError = error)
}

package com.example.design_system.containers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * A non-visual lifecycle observer for [LazyPagingItems] that streamlines load state management.
 *
 * This utility extracts the "Refresh" state logic into simple callbacks, allowing
 * parent Composables to react to loading or error states (e.g., showing a global
 * progress bar or a Snackbar) without nesting complex when-statements in the UI code.
 *
 * @param T The type of data being paged.
 * @param items The [LazyPagingItems] instance to observe.
 * @param onLoadingChange Callback triggered when the refresh state starts or stops loading.
 * @param onErrorChange Callback triggered when the refresh state enters or leaves an error state.
 * @param onRetryRequest Callback triggered specifically when an error occurs, providing
 * the error message and a retry function to re-trigger the pager.
 */
@Composable
fun <T : Any> PagingStatesContainer(
    items: LazyPagingItems<T>,
    onLoadingChange: (Boolean) -> Unit = {},
    onErrorChange: (Boolean) -> Unit = {},
    onRetryRequest: (label: String, retry: () -> Unit) -> Unit,
) {
    val currentOnLoadingChange by rememberUpdatedState(onLoadingChange)
    val currentOnErrorChange by rememberUpdatedState(onErrorChange)
    val currentOnRetryRequest by rememberUpdatedState(onRetryRequest)

    // Only values that we need
    val loadState = items.loadState.refresh
    val isLoading = loadState is LoadState.Loading
    val isError = loadState is LoadState.Error
    val errorMessage = (loadState as? LoadState.Error)?.error?.message

    // 3. LaunchedEffect observe only 3 values that we need
    LaunchedEffect(isLoading, isError, errorMessage) {
        currentOnLoadingChange(isLoading)
        currentOnErrorChange(isError)

        if (isError && errorMessage != null) {
            currentOnRetryRequest(errorMessage) { items.retry() }
        }
    }
}
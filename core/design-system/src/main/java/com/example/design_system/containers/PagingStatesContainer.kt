package com.example.design_system.containers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
inline fun <T : Any> PagingStatesContainer(
    items: LazyPagingItems<T>,
    crossinline onLoadingChange: (Boolean) -> Unit = {},
    crossinline onErrorChange: (Boolean) -> Unit = {},
    crossinline onRetryRequest: (label: String, retry: () -> Unit) -> Unit,
) {
    val refreshState = items.loadState.refresh

    // Single effect handles loading, error, and retry logic
    LaunchedEffect(refreshState) {

        // Report loading state
        onLoadingChange(refreshState is LoadState.Loading)

        // Report error state
        val isError = refreshState is LoadState.Error
        onErrorChange(isError)

        // Handle error retry snackbar
        if (isError) {
            val error = refreshState.error
            val message = error.message ?: return@LaunchedEffect
            onRetryRequest(message) { items.retry() }
        }
    }
}
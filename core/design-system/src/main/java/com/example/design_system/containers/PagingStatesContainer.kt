package com.example.design_system.containers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

// Common function to handle paging states
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
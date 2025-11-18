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

    LaunchedEffect(refreshState) {
        val err = (refreshState as? LoadState.Error)?.error ?: return@LaunchedEffect
        onRetryRequest(err.message!!) { items.retry() }
    }

    LaunchedEffect(refreshState) {
        onLoadingChange(refreshState is LoadState.Loading)
    }

    LaunchedEffect(refreshState) {
        onErrorChange(refreshState is LoadState.Error)
    }
}
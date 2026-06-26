package com.brbx.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import com.brbx.domain.network.paging.model.PagingException

@Composable
internal fun PagingStatesHandler(handler: PagingHandler) {
    val currentOnException by rememberUpdatedState(newValue = handler.onException)
    val currentOnLoading by rememberUpdatedState(newValue = handler.onLoading)

    var isFirstLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = handler.loadState) {
        when (val state = handler.loadState) {
            is LoadState.Loading -> {
                currentOnLoading(isFirstLoading, true)
            }
            is LoadState.NotLoading -> {
                currentOnLoading(isFirstLoading, false)
                if (isFirstLoading) {
                    isFirstLoading = false
                }
            }
            is LoadState.Error -> {
                currentOnLoading(isFirstLoading, false)

                val error = state.error as? PagingException
                if (error != null) {
                    currentOnException(isFirstLoading, error)
                }
            }
        }
    }
}
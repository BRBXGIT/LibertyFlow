package com.brbx.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import com.brbx.common.view_model.model.intent.CommonPagingIntent
import com.brbx.domain.network.paging.model.PagingException

@Stable
data class PagingHandler(
    val loadState: LoadState,
    val dispatchIntent: (CommonPagingIntent.Loading) -> Unit,
)

@Composable
internal fun PagingStatesHandler(handler: PagingHandler) {
    var isFirstLoading by rememberSaveable { mutableStateOf(value = true) }

    LaunchedEffect(key1 = handler.loadState) {
        val loadState = handler.loadState
        val dispatch = handler.dispatchIntent
        if (isFirstLoading) {
            when (loadState) {
                is LoadState.Loading -> {
                    dispatch(CommonPagingIntent.Loading.LoadingIntent.SetLoading(true))
                    dispatch(CommonPagingIntent.Loading.LoadingIntent.SetException(false))
                }
                is LoadState.NotLoading -> {
                    dispatch(CommonPagingIntent.Loading.LoadingIntent.SetLoading(false))
                    isFirstLoading = false
                }
                is LoadState.Error -> {
                    dispatch(CommonPagingIntent.Loading.LoadingIntent.SetLoading(false))
                    val exception = loadState.error as PagingException
                    dispatch(CommonPagingIntent.Loading.LoadingIntent.SetException(true, exception))
                }
            }
        } else {
            when (loadState) {
                is LoadState.Loading -> {
                    dispatch(CommonPagingIntent.Loading.RefreshIntent.SetRefreshing(true))
                    dispatch(CommonPagingIntent.Loading.RefreshIntent.SetException(false))
                }
                is LoadState.NotLoading -> {
                    dispatch(CommonPagingIntent.Loading.RefreshIntent.SetRefreshing(false))
                }
                is LoadState.Error -> {
                    dispatch(CommonPagingIntent.Loading.RefreshIntent.SetRefreshing(false))
                    val exception = loadState.error as PagingException
                    dispatch(CommonPagingIntent.Loading.RefreshIntent.SetException(true, exception))
                }
            }
        }
    }
}
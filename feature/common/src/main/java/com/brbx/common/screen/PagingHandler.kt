package com.brbx.common.screen

import androidx.compose.runtime.Stable
import androidx.paging.LoadState
import com.brbx.domain.network.paging.model.PagingException

@Stable
data class PagingHandler(
    val loadState: LoadState,
    val onException: (isFirstLoading: Boolean, exception: PagingException) -> Unit,
    val onLoading: (isFirstLoading: Boolean, isLoading: Boolean) -> Unit,
)
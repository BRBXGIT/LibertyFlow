package com.brbx.common.view_model.processor.paging.model

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import arrow.optics.optics
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
@optics
data class CommonPagingState<PagingItem : Any>(
    val loading: CommonLoadingState = CommonLoadingState(isLoading = true),
    val refreshing: CommonLoadingState = CommonLoadingState(),
    val pagingData: Flow<PagingData<PagingItem>> = emptyFlow(),
) { companion object }
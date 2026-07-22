package com.brbx.common.view_model.processor.paging.processor

import androidx.paging.PagingData
import androidx.paging.cachedIn
import arrow.optics.Lens
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.processor.paging.model.CommonPagingState
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.milliseconds

internal class CommonPagingProcessorImpl<State, PagingItem : Any, Params>(
    private val pagingLens: Lens<State, CommonPagingState<PagingItem>>,
    private val paramsSelector: (State) -> Params,
    private val pagingDataFactory: (Params) -> Flow<PagingData<PagingItem>>,
    private val debounceMillis: Long,
) : LibertyFlowIntentProcessor<State, CommonPagingIntent>(),
    CommonPagingProcessor<State> {

    override fun process(intent: CommonPagingIntent) {
        when (intent) {
            is CommonPagingIntent.SetUpPaging -> handleSetUpPaging()
            is CommonPagingIntent.Loading.LoadingIntent.SetLoading -> setLoading(intent.loading)
            is CommonPagingIntent.Loading.LoadingIntent.SetException -> setLoadingException(intent)
            is CommonPagingIntent.Loading.RefreshIntent.SetRefreshing -> setRefreshing(intent.refreshing)
            is CommonPagingIntent.Loading.RefreshIntent.SetException -> setRefreshException(intent)
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun handleSetUpPaging() {
        val pagingFlow = state
            .map { paramsSelector(it) }
            .distinctUntilChanged()
            .debounce(timeout = debounceMillis.milliseconds)
            .flatMapLatest { params -> pagingDataFactory(params) }
            .cachedIn(coroutineScope)

        updatePagingState { it.copy(pagingData = pagingFlow) }
    }

    private fun setLoading(isLoading: Boolean) {
        updatePagingState { it.copy(loading = it.loading.copy(isLoading = isLoading)) }
    }

    private fun setLoadingException(
        intent: CommonPagingIntent.Loading.LoadingIntent.SetException
    ) {
        updatePagingState {
            it.copy(loading = it.loading.copy(isException = intent.isException))
        }

        intent.exception?.let { exception ->
            postExceptionSnackbar(exception = exception.toBrbxText()) {
                process(intent = CommonPagingIntent.SetUpPaging)
            }
        }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        updatePagingState {
            it.copy(refreshing = it.refreshing.copy(isLoading = isRefreshing))
        }
    }

    private fun setRefreshException(
        intent: CommonPagingIntent.Loading.RefreshIntent.SetException
    ) {
        updatePagingState {
            it.copy(refreshing = it.refreshing.copy(isException = intent.isException))
        }

        intent.exception?.let { exception ->
            if (intent.withRetry) {
                postExceptionSnackbar(exception = exception.toBrbxText()) {
                    process(intent = CommonPagingIntent.SetUpPaging)
                }
            } else {
                postExceptionSnackbar(exception = exception.toBrbxText())
            }
        }
    }

    private fun updatePagingState(
        map: (CommonPagingState<PagingItem>) -> CommonPagingState<PagingItem>
    ) { updateLensState(pagingLens, map) }
}
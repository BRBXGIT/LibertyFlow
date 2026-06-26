package com.brbx.common.view_model.processor.paging

import androidx.paging.PagingData
import androidx.paging.cachedIn
import arrow.optics.Lens
import com.brbx.common.view_model.model.intent.CommonPagingIntent
import com.brbx.common.view_model.model.state.CommonPagingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CommonPagingProcessorImpl<State, PagingItem : Any, Params>(
    private val pagingLens: Lens<State, CommonPagingState<PagingItem>>,
    private val paramsSelector: (State) -> Params,
    private val pagingDataFactory: (Params) -> Flow<PagingData<PagingItem>>
) : CommonPagingProcessor<State> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun LibertyFlowMviScope<State>.process(intent: CommonPagingIntent) {
        when (intent) {
            CommonPagingIntent.SetUpPaging -> {
                val pagingFlow = state
                    .map { paramsSelector(it) }
                    .distinctUntilChanged()
                    .flatMapLatest { params ->
                        pagingDataFactory(params)
                    }
                    .cachedIn(coroutineScope)

                updateState {
                    pagingLens.modify(source = this) { it.copy(pagingData = pagingFlow) }
                }
            }
            is CommonPagingIntent.Loading -> {
                when (intent) {
                    is CommonPagingIntent.Loading.LoadingIntent -> {
                        when (intent) {
                            is CommonPagingIntent.Loading.LoadingIntent.SetLoading -> {
                                updateState {
                                    pagingLens.modify(source = this) {
                                        it.copy(
                                            loading =
                                                it.loading.copy(isLoading = intent.loading)
                                        )
                                    }
                                }
                            }
                            is CommonPagingIntent.Loading.LoadingIntent.SetException -> {
                                updateState {
                                    pagingLens.modify(source = this) {
                                        it.copy(
                                            loading =
                                                it.loading.copy(isException = intent.exception)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is CommonPagingIntent.Loading.RefreshIntent -> {
                        when (intent) {
                            is CommonPagingIntent.Loading.RefreshIntent.SetException -> {
                                updateState {
                                    pagingLens.modify(source = this) {
                                        it.copy(
                                            refreshing =
                                                it.refreshing.copy(isException = intent.exception)
                                        )
                                    }
                                }
                            }
                            is CommonPagingIntent.Loading.RefreshIntent.SetRefreshing -> {
                                updateState {
                                    pagingLens.modify(source = this) {
                                        it.copy(
                                            refreshing =
                                                it.refreshing.copy(isLoading = intent.refreshing)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
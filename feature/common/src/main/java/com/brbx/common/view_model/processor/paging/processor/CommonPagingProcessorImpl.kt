package com.brbx.common.view_model.processor.paging.processor

import androidx.paging.PagingData
import androidx.paging.cachedIn
import arrow.optics.Lens
import com.brbx.common.strings.toBrbxText
import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.processor.paging.model.CommonPagingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.postNetworkExceptionSnackbar
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
) : CommonPagingProcessor<State> {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun LibertyFlowMviScope<State>.process(intent: CommonPagingIntent) {
        when (intent) {
            CommonPagingIntent.SetUpPaging -> {
                val pagingFlow = state
                    .map { paramsSelector(it) }
                    .distinctUntilChanged()
                    .debounce(timeout = debounceMillis.milliseconds)
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
                                                it.loading.copy(isException = intent.isException)
                                        )
                                    }
                                }
                                val exception = intent.exception
                                if (exception != null) {
                                    postNetworkExceptionSnackbar(
                                        exception = exception.toBrbxText(),
                                    ) { process(intent = CommonPagingIntent.SetUpPaging) }
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
                                                it.refreshing.copy(isException = intent.isException)
                                        )
                                    }
                                }
                                val exception = intent.exception
                                if (exception != null) {
                                    val onButtonClick: (() -> Unit)? = if (intent.withRetry) {
                                        { process(intent = CommonPagingIntent.SetUpPaging) }
                                    } else null
                                    postNetworkExceptionSnackbar(
                                        exception = exception.toBrbxText(),
                                        onButtonClick = onButtonClick,
                                    )
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
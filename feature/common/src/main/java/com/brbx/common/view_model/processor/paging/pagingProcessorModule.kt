package com.brbx.common.view_model.processor.paging

import androidx.paging.PagingData
import arrow.optics.Lens
import com.brbx.common.view_model.model.state.CommonPagingState
import kotlinx.coroutines.flow.Flow
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val pagingProcessorModule = module {
    factory<CommonPagingProcessor<*>> { params ->
        CommonPagingProcessorImpl<Any, Any, Any>(
            pagingLens = params[0],
            paramsSelector = params[1],
            pagingDataFactory = params[2]
        )
    }
}

inline fun <reified State, reified PagingItem : Any, reified Params> Scope.getCommonPagingProcessor(
    lens: Lens<State, CommonPagingState<PagingItem>>,
    noinline paramsSelector: (State) -> Params,
    noinline pagingDataFactory: (Params) -> Flow<PagingData<PagingItem>>
): CommonPagingProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonPagingProcessor<*>> {
        parametersOf(
            lens as Lens<Any, CommonPagingState<Any>>,
            paramsSelector as (Any) -> Any,
            pagingDataFactory as (Any) -> Flow<PagingData<Any>>,
        )
    } as CommonPagingProcessor<State>
}
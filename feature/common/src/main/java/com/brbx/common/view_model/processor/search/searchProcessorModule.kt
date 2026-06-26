package com.brbx.common.view_model.processor.search

import arrow.optics.Lens
import com.brbx.common.view_model.model.state.CommonSearchState
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val searchProcessorModule = module {
    factory<CommonSearchProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonSearchState>>()
        CommonSearchProcessorImpl(searchLens = lens)
    }
}

inline fun <reified State> Scope.getCommonSearchProcessor(
    lens: Lens<State, CommonSearchState>
): CommonSearchProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonSearchProcessor<*>> { parametersOf(lens) } as CommonSearchProcessor<State>
}
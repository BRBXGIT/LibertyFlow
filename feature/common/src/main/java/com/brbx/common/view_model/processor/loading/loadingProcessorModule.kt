package com.brbx.common.view_model.processor.loading

import arrow.optics.Lens
import com.brbx.common.view_model.model.state.CommonLoadingState
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val loadingProcessorModule = module {
    factory<CommonLoadingProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonLoadingState>>()
        CommonLoadingProcessorImpl(loadingLens = lens)
    }
}

inline fun <reified State> Scope.getCommonLoadingProcessor(
    lens: Lens<State, CommonLoadingState>
): CommonLoadingProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonLoadingProcessor<*>> { parametersOf(lens) } as CommonLoadingProcessor<State>
}
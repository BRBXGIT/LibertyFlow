package com.brbx.common.view_model.processor.auth

import arrow.optics.Lens
import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetState
import com.brbx.common.view_model.processor.auth.processor.CommonAuthSheetProcessor
import com.brbx.common.view_model.processor.auth.processor.CommonAuthSheetProcessorImpl
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val authSheetProcessorModule = module {
    factory<CommonAuthSheetProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonAuthSheetState>>()
        CommonAuthSheetProcessorImpl(
            authSheetLens = lens,
            authUseCase = get(),
            dispatcherIo = getDispatcherIo(),
        )
    }
}

inline fun <reified State> Scope.getCommonAuthSheetProcessor(
    lens: Lens<State, CommonAuthSheetState>,
): CommonAuthSheetProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonAuthSheetProcessor<*>> { parametersOf(lens) } as CommonAuthSheetProcessor<State>
}
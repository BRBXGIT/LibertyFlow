package com.brbx.common.view_model.processor.selection

import arrow.optics.Lens
import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.common.view_model.processor.selection.processor.CommonSelectionProcessor
import com.brbx.common.view_model.processor.selection.processor.CommonSelectionProcessorImpl
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val selectionProcessorModule = module {
    factory<CommonSelectionProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonSelectionState>>()
        val onUnauthorized = params.getOrNull<LibertyFlowMviScope<Any>.() -> Unit>() ?: {}

        CommonSelectionProcessorImpl(
            addToFavoritesUseCase = get(),
            deleteFromFavoritesUseCase = get(),
            addToCollectionUseCase = get(),
            deleteFromCollectionUseCase = get(),
            selectionLens = lens,
            dispatcherIo = getDispatcherIo(),
            onUnauthorized = onUnauthorized,
        )
    }
}

inline fun <reified State> Scope.getCommonSelectionProcessor(
    lens: Lens<State, CommonSelectionState>,
    noinline onUnauthorized: LibertyFlowMviScope<State>.() -> Unit = {},
): CommonSelectionProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonSelectionProcessor<*>> {
        parametersOf(lens, onUnauthorized)
    } as CommonSelectionProcessor<State>
}

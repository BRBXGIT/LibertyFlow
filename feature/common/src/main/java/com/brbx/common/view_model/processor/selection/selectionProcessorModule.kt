package com.brbx.common.view_model.processor.selection

import arrow.optics.Lens
import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.common.view_model.processor.selection.processor.CommonSelectionProcessor
import com.brbx.common.view_model.processor.selection.processor.CommonSelectionProcessorImpl
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val selectionProcessorModule = module {
    factory<CommonSelectionProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonSelectionState>>()

        CommonSelectionProcessorImpl(
            addToFavoritesUseCase = get(),
            deleteFromFavoritesUseCase = get(),
            addToCollectionUseCase = get(),
            deleteFromCollectionUseCase = get(),
            selectionLens = lens,
            dispatcherIo = getDispatcherIo(),
        )
    }
}

inline fun <reified State> Scope.getCommonSelectionProcessor(
    lens: Lens<State, CommonSelectionState>,
): CommonSelectionProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonSelectionProcessor<*>> {
        parametersOf(lens)
    } as CommonSelectionProcessor<State>
}

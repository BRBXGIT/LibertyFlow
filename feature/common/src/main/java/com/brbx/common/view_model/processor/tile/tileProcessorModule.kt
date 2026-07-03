package com.brbx.common.view_model.processor.tile

import arrow.optics.Lens
import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.processor.interactor.tileInteractorModule
import com.brbx.common.view_model.processor.tile.processor.processor.CommonTileProcessor
import com.brbx.common.view_model.processor.tile.processor.processor.CommonTileProcessorImpl
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val tileProcessorModule = module {
    factory<CommonTileProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonTileState?>>()
        CommonTileProcessorImpl(
            tileLens = lens,
            dispatcherIo = getDispatcherIo(),
            episodeInteractor = get(),
            recommendationInteractor = get(),
            stubInteractor = get(),
        )
    }

    includes(tileInteractorModule)
}

inline fun <reified State> Scope.getCommonTileProcessor(
    lens: Lens<State, CommonTileState?>
): CommonTileProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonTileProcessor<*>> { parametersOf(lens) } as CommonTileProcessor<State>
}
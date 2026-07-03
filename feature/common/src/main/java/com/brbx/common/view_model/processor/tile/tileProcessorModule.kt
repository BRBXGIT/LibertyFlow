package com.brbx.common.view_model.processor.tile

import arrow.optics.Lens
import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.processor.CommonTileProcessor
import com.brbx.common.view_model.processor.tile.processor.CommonTileProcessorImpl
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val tileProcessorModule = module {
    factory<CommonTileProcessor<*>> { params ->
        val lens = params.get<Lens<Any, CommonTileState>>()
        CommonTileProcessorImpl(
            tileLens = lens,
            getLatestWatchingAnimeUseCase = get(),
            dispatcherIo = getDispatcherIo(),
        )
    }
}

inline fun <reified State> Scope.getCommonTileProcessor(
    lens: Lens<State, CommonTileState>
): CommonTileProcessor<State> {
    @Suppress("UNCHECKED_CAST")
    return get<CommonTileProcessor<*>> { parametersOf(lens) } as CommonTileProcessor<State>
}
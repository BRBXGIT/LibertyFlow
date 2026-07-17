package com.brbx.home.view_model.processor.tile.processor

import com.brbx.common.dispatchers.getDispatcherIo
import org.koin.dsl.module

internal val tileProcessorModule = module {
    single<TileProcessor> {
        TileProcessorImpl(
            stubInteractor = get(),
            latestWatchedTileInteractor = get(),
            dispatcherIo = getDispatcherIo(),
        )
    }
}
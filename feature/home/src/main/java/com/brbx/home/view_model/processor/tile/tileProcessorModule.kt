package com.brbx.home.view_model.processor.tile

import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.home.view_model.processor.tile.interactor.tileInteractorsModule
import org.koin.dsl.module

internal val tileProcessorModule = module {
    single<TileProcessor> {
        TileProcessorImpl(
            homeTileInteractor = get(),
            dispatcherIo = getDispatcherIo(),
        )
    }
    includes(tileInteractorsModule)
}

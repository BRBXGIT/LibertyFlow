package com.brbx.home.view_model.processor.tile

import com.brbx.home.view_model.processor.tile.interactor.homeTileInteractorsModule
import com.brbx.home.view_model.processor.tile.processor.processorModule
import org.koin.dsl.module

internal val tileProcessorModule = module {
    includes(
        processorModule,
        homeTileInteractorsModule,
    )
}
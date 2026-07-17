package com.brbx.home.view_model.processor.tile

import com.brbx.home.view_model.processor.tile.interactor.tileInteractorsModule
import com.brbx.home.view_model.processor.tile.processor.tileProcessorModule
import org.koin.dsl.module

internal val tileProcessorModule = module {
    includes(
        tileProcessorModule,
        tileInteractorsModule,
    )
}
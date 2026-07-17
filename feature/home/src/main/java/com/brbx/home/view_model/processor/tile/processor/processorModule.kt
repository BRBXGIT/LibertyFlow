package com.brbx.home.view_model.processor.tile.processor

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val processorModule = module {
    singleOf(constructor = ::TileProcessorImpl) { bind<TileProcessor>() }
}
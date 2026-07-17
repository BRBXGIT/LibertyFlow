package com.brbx.home.view_model.processor

import com.brbx.home.view_model.processor.filters.filtersProcessorModule
import com.brbx.home.view_model.processor.random_anime.randomAnimeProcessorModule
import com.brbx.home.view_model.processor.tile.tileProcessorModule
import org.koin.dsl.module

internal val homeProcessorsModule = module {
    includes(
        filtersProcessorModule,
        randomAnimeProcessorModule,
        tileProcessorModule,
    )
}
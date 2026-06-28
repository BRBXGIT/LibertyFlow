package com.brbx.home.view_model.processor

import com.brbx.common.dispatchers.DispatcherQualifier
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.filters.FiltersProcessorImpl
import com.brbx.home.view_model.processor.tile_processor.TileProcessor
import com.brbx.home.view_model.processor.tile_processor.TileProcessorImpl
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val processorsModule = module {
    singleOf(constructor = ::FiltersProcessorImpl) { bind<FiltersProcessor>() }
    singleOf(constructor = ::RandomAnimeProcessorImpl) { bind<RandomAnimeProcessor>() }
    single<TileProcessor> {
        TileProcessorImpl(
            latestWatchedAnimeUseCase = get(),
            dispatcherIo = get(qualifier = DispatcherQualifier.Io)
        )
    }
    single<RandomAnimeProcessor> {
        RandomAnimeProcessorImpl(
            dispatcherIo = get(qualifier = DispatcherQualifier.Io),
            randomAnimeUseCase = get(),
        )
    }
}
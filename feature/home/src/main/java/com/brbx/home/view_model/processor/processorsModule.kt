package com.brbx.home.view_model.processor

import com.brbx.common.dispatchers.getDispatcherIo
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.filters.FiltersProcessorImpl
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessorImpl
import com.brbx.home.view_model.processor.tile_processor.TileProcessor
import com.brbx.home.view_model.processor.tile_processor.TileProcessorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val processorsModule = module {
    singleOf(constructor = ::RandomAnimeProcessorImpl) { bind<RandomAnimeProcessor>() }
    single<FiltersProcessor> {
        FiltersProcessorImpl(
            genresUseCase = get(),
            dispatcherIo = getDispatcherIo(),
        )
    }
    single<TileProcessor> {
        TileProcessorImpl(
            latestWatchedAnimeUseCase = get(),
            dispatcherIo = getDispatcherIo()
        )
    }
    single<RandomAnimeProcessor> {
        RandomAnimeProcessorImpl(
            dispatcherIo = getDispatcherIo(),
            randomAnimeUseCase = get(),
        )
    }
}
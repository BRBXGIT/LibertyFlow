package com.brbx.domain.local_dbs.watching_anime.use_case.episodes.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val watchedEpisodesUseCaseModule = module {
    factoryOf(constructor = ::GetWatchedEpisodesIndexesUseCase)
    factoryOf(constructor = ::GetWatchedEpisodeProgressUseCase)
    factoryOf(constructor = ::UpsertEpisodeToWatchedUseCase)
}
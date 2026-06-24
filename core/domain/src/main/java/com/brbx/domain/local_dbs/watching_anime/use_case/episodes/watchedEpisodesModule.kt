package com.brbx.domain.local_dbs.watching_anime.use_case.episodes

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val watchedEpisodesModule = module {
    factoryOf(constructor = ::GetWatchedEpisodesIndexesUseCase)
    factoryOf(constructor = ::GetWatchedEpisodeProgressUseCase)
    factoryOf(constructor = ::UpsertEpisodeToWatchedUseCase)
}
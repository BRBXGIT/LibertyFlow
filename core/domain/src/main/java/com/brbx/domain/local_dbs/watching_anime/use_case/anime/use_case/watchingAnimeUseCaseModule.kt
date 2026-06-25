package com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val watchingAnimeUseCaseModule = module {
    factoryOf(constructor = ::GetLatestWatchingAnimeUseCase)
    factoryOf(constructor = ::InsertWatchingAnimeUseCase)
}
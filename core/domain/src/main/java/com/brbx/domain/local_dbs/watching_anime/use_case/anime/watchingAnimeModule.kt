package com.brbx.domain.local_dbs.watching_anime.use_case.anime

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val watchingAnimeModule = module {
    factoryOf(constructor = ::GetLatestWatchingAnimeUseCase)
    factoryOf(constructor = ::InsertWatchingAnimeUseCase)
}
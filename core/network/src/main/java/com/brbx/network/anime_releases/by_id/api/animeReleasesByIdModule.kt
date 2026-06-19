package com.brbx.network.anime_releases.by_id.api

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeReleasesByIdApiModule = module {
    singleOf(constructor = ::AnimeReleasesByIdApiImpl) { bind<AnimeReleasesByIdApi>() }
}
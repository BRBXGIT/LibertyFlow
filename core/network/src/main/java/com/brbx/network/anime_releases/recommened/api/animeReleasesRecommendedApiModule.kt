package com.brbx.network.anime_releases.recommened.api

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeReleasesRecommendedApiModule = module {
    singleOf(constructor = ::AnimeReleasesRecommendedApiImpl) { bind<AnimeReleasesRecommendedApi>() }
}
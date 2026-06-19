package com.brbx.network.anime_releases.random

import com.brbx.network.anime_releases.random.api.AnimeReleasesRandomApi
import com.brbx.network.anime_releases.random.api.AnimeReleasesRandomApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeReleasesRandomApiModule = module {
    singleOf(constructor = ::AnimeReleasesRandomApiImpl) { bind<AnimeReleasesRandomApi>() }
}
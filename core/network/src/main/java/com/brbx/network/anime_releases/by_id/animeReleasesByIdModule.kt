package com.brbx.network.anime_releases.by_id

import com.brbx.network.anime_releases.by_id.api.AnimeReleasesByIdApi
import com.brbx.network.anime_releases.by_id.api.AnimeReleasesByIdApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeReleasesByIdApiModule = module {
    singleOf(constructor = ::AnimeReleasesByIdApiImpl) { bind<AnimeReleasesByIdApi>() }
}
package com.brbx.network.anime_releases.recommened

import com.brbx.network.anime_releases.by_id.animeReleasesByIdApiModule
import com.brbx.network.anime_releases.random.animeReleasesRandomApiModule
import com.brbx.network.anime_releases.recommened.api.animeReleasesRecommendedApiModule
import org.koin.dsl.module

internal val animeReleasesModule = module {
    includes(
        animeReleasesByIdApiModule,
        animeReleasesRandomApiModule,
        animeReleasesRecommendedApiModule,
    )
}
package com.brbx.network.anime_releases

import com.brbx.network.anime_releases.by_id.api.animeReleasesByIdApiModule
import com.brbx.network.anime_releases.random.api.animeReleasesRandomApiModule
import com.brbx.network.anime_releases.recommened.api.animeReleasesRecommendedApiModule
import org.koin.dsl.module

internal val animeReleasesModule = module {
    includes(
        animeReleasesByIdApiModule,
        animeReleasesRandomApiModule,
        animeReleasesRecommendedApiModule,
    )
}
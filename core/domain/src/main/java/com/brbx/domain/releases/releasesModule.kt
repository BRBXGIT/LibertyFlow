package com.brbx.domain.releases

import com.brbx.domain.releases.by_id.use_case.getAnimeReleaseByIdUseCaseModule
import com.brbx.domain.releases.random.use_case.getRandomAnimeReleaseUseCaseModule
import com.brbx.domain.releases.recommended.use_case.getRecommendedAnimeReleasesUseCaseModule
import org.koin.dsl.module

internal val releasesModule = module {
    includes(
        getAnimeReleaseByIdUseCaseModule,
        getRandomAnimeReleaseUseCaseModule,
        getRecommendedAnimeReleasesUseCaseModule,
    )
}
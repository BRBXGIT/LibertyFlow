package com.brbx.data.releases

import com.brbx.data.releases.by_id.repository.animeReleaseByIdRepositoryModule
import com.brbx.data.releases.random.randomAnimeReleaseRepositoryModule
import com.brbx.data.releases.recommended.recommendedAnimeReleasesRepositoryModule
import org.koin.dsl.module

internal val releasesModule = module {
    includes(
        animeReleaseByIdRepositoryModule,
        randomAnimeReleaseRepositoryModule,
        recommendedAnimeReleasesRepositoryModule,
    )
}
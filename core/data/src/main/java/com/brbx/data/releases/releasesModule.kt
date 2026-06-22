package com.brbx.data.releases

import com.brbx.data.releases.by_id.repository.animeReleaseByIdRepositoryModule
import com.brbx.data.releases.random.repository.randomAnimeReleaseRepositoryModule
import com.brbx.data.releases.recommended.repository.recommendedAnimeReleasesRepositoryModule
import org.koin.dsl.module

internal val releasesModule = module {
    includes(
        animeReleaseByIdRepositoryModule,
        randomAnimeReleaseRepositoryModule,
        recommendedAnimeReleasesRepositoryModule,
    )
}
package com.brbx.data.network.releases

import com.brbx.data.network.releases.by_id.repository.animeReleaseByIdRepositoryModule
import com.brbx.data.network.releases.random.repository.randomAnimeReleaseRepositoryModule
import com.brbx.data.network.releases.recommended.repository.recommendedAnimeReleasesRepositoryModule
import org.koin.dsl.module

internal val releasesModule = module {
    includes(
        animeReleaseByIdRepositoryModule,
        randomAnimeReleaseRepositoryModule,
        recommendedAnimeReleasesRepositoryModule,
    )
}
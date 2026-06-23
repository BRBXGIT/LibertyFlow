package com.brbx.data.network.releases.recommended.repository

import com.brbx.domain.network.releases.recommended.repository.RecommendedAnimeReleasesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val recommendedAnimeReleasesRepositoryModule = module {
    singleOf(constructor = ::RecommendedAnimeReleasesRepositoryImpl) {
        bind<RecommendedAnimeReleasesRepository>()
    }
}
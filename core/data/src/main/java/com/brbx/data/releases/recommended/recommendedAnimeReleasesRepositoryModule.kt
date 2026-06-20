package com.brbx.data.releases.recommended

import com.brbx.domain.releases.recommended.repository.RecommendedAnimeReleasesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val recommendedAnimeReleasesRepositoryModule = module {
    singleOf(constructor = ::RecommendedAnimeReleasesRepositoryImpl) {
        bind<RecommendedAnimeReleasesRepository>()
    }
}
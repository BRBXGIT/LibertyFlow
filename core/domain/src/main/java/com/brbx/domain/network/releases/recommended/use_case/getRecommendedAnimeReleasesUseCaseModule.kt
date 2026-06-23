package com.brbx.domain.network.releases.recommended.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getRecommendedAnimeReleasesUseCaseModule = module {
    factoryOf(constructor = ::GetRecommendedAnimeReleasesUseCase)
}
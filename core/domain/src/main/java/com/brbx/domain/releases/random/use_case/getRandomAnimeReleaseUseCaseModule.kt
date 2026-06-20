package com.brbx.domain.releases.random.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getRandomAnimeReleaseUseCaseModule = module {
    factoryOf(constructor = ::GetRandomAnimeReleaseUseCase)
}
package com.brbx.domain.network.releases.by_id.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getAnimeReleaseByIdUseCaseModule = module {
    factoryOf(constructor = ::GetAnimeReleaseByIdUseCase)
}
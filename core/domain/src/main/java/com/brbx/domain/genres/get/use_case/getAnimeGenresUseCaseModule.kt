package com.brbx.domain.genres.get.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getAnimeGenresUseCaseModule = module {
    factoryOf(constructor = ::GetAnimeGenresUseCase)
}
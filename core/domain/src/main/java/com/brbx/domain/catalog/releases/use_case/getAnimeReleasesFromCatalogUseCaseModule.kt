package com.brbx.domain.catalog.releases.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getCatalogAnimeReleasesUseCaseModule = module {
    factoryOf(constructor = ::GetCatalogAnimeReleasesUseCase)
}
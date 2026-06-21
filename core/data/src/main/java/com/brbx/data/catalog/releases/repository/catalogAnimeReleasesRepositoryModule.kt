package com.brbx.data.catalog.releases.repository

import com.brbx.domain.catalog.releases.repository.CatalogAnimeReleasesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val catalogAnimeReleasesRepositoryModule = module {
    singleOf(constructor = ::CatalogAnimeReleasesRepositoryImpl) {
        bind<CatalogAnimeReleasesRepository>()
    }
}
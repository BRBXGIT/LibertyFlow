package com.brbx.data.network.catalog.releases.repository

import com.brbx.domain.network.catalog.releases.repository.CatalogAnimeReleasesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val catalogAnimeReleasesRepositoryModule = module {
    singleOf(constructor = ::CatalogAnimeReleasesRepositoryImpl) {
        bind<CatalogAnimeReleasesRepository>()
    }
}
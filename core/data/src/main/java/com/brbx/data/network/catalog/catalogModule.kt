package com.brbx.data.network.catalog

import com.brbx.data.network.catalog.releases.repository.catalogAnimeReleasesRepositoryModule
import org.koin.dsl.module

internal val catalogModule = module {
    includes(
        catalogAnimeReleasesRepositoryModule,
    )
}
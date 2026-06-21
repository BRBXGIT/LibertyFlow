package com.brbx.data.catalog

import com.brbx.data.catalog.releases.repository.catalogAnimeReleasesRepositoryModule
import org.koin.dsl.module

internal val catalogModule = module {
    includes(
        catalogAnimeReleasesRepositoryModule,
    )
}
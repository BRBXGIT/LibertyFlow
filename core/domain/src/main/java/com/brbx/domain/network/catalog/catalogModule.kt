package com.brbx.domain.network.catalog

import com.brbx.domain.network.catalog.releases.use_case.getCatalogAnimeReleasesUseCaseModule
import org.koin.dsl.module

internal val catalogModule = module {
    includes(
        getCatalogAnimeReleasesUseCaseModule,
    )
}
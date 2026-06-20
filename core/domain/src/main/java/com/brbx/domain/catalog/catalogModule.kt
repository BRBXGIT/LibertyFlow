package com.brbx.domain.catalog

import com.brbx.domain.catalog.releases.use_case.getCatalogAnimeReleasesUseCaseModule
import org.koin.dsl.module

internal val catalogModule = module {
    includes(
        getCatalogAnimeReleasesUseCaseModule,
    )
}
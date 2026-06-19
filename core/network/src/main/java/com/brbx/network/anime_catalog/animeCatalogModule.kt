package com.brbx.network.anime_catalog

import com.brbx.network.anime_catalog.releases.animeCatalogReleasesApiModule
import org.koin.dsl.module

internal val animeCatalogModule = module {
    includes(
        animeCatalogReleasesApiModule,
    )
}
package com.brbx.network

import com.brbx.network.anime_catalog.animeCatalogModule
import com.brbx.network.anime_releases.animeReleasesModule
import com.brbx.network.base.apiModule
import org.koin.dsl.module

val networkModule = module {
    includes(
        animeCatalogModule,
        animeReleasesModule,
        apiModule,
    )
}
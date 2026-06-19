package com.brbx.network.anime_catalog.releases.api

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeCatalogReleasesApiModule = module {
    singleOf(constructor = ::AnimeCatalogReleasesApiImpl) { bind<AnimeCatalogReleasesApi>() }
}
package com.brbx.domain.network

import com.brbx.domain.network.catalog.catalogModule
import com.brbx.domain.network.genres.genresModule
import com.brbx.domain.network.releases.releasesModule
import com.brbx.domain.network.user.userModule
import org.koin.dsl.module

internal val networkModule = module {
    includes(
        genresModule,
        catalogModule,
        releasesModule,
        userModule,
    )
}
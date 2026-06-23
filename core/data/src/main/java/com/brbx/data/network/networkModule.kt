package com.brbx.data.network

import com.brbx.data.network.api.apiModule
import com.brbx.data.network.catalog.catalogModule
import com.brbx.data.network.genres.genresModule
import com.brbx.data.network.releases.releasesModule
import com.brbx.data.network.user.userModule
import org.koin.dsl.module

internal val networkModule = module {
    includes(
        apiModule,
        catalogModule,
        genresModule,
        releasesModule,
        userModule,
    )
}
package com.brbx.data

import com.brbx.data.api.apiModule
import com.brbx.data.appearance.appearanceModule
import com.brbx.data.catalog.catalogModule
import com.brbx.data.genres.genresModule
import com.brbx.data.releases.releasesModule
import com.brbx.data.user.userModule
import org.koin.dsl.module

val dataModule = module {
    includes(
        apiModule,
        catalogModule,
        genresModule,
        releasesModule,
        userModule,
        appearanceModule,
    )
}
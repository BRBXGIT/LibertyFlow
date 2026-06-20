package com.brbx.data

import com.brbx.data.genres.genresModule
import com.brbx.data.releases.releasesModule
import org.koin.dsl.module

val dataModule = module {
    includes(
        genresModule,
        releasesModule,
    )
}
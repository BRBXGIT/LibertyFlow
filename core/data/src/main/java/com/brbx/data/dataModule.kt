package com.brbx.data

import com.brbx.data.genres.genresModule
import org.koin.dsl.module

val dataModule = module {
    includes(
        genresModule,
    )
}
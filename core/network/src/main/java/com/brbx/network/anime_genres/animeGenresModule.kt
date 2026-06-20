package com.brbx.network.anime_genres

import com.brbx.network.anime_genres.genres.genresModule
import org.koin.dsl.module

internal val animeGenresModule = module {
    includes(
        genresModule,
    )
}
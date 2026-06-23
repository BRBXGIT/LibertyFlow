package com.brbx.data.network.genres

import com.brbx.data.network.genres.get.repository.animeGenresRepositoryModule
import org.koin.dsl.module

internal val genresModule = module {
    includes(
        animeGenresRepositoryModule,
    )
}
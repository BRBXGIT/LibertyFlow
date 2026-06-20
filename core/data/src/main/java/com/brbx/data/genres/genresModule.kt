package com.brbx.data.genres

import com.brbx.data.genres.get.repository.animeGenresRepositoryModule
import org.koin.dsl.module

internal val genresModule = module {
    includes(
        animeGenresRepositoryModule,
    )
}
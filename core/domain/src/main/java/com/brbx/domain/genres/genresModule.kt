package com.brbx.domain.genres

import com.brbx.domain.genres.get_genres.use_case.getAnimeGenresUseCaseModule
import org.koin.dsl.module

internal val genresModule = module {
    includes(
        getAnimeGenresUseCaseModule,
    )
}
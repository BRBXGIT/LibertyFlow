package com.brbx.domain.genres

import com.brbx.domain.genres.use_case.getAnimeGenresUseCaseModule
import org.koin.dsl.module

internal val genresModule = module {
    includes(
        getAnimeGenresUseCaseModule,
    )
}
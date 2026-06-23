package com.brbx.domain.network.genres

import com.brbx.domain.network.genres.get.use_case.getAnimeGenresUseCaseModule
import org.koin.dsl.module

internal val genresModule = module {
    includes(
        getAnimeGenresUseCaseModule,
    )
}
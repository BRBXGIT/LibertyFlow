package com.brbx.data.network.genres.get.repository

import com.brbx.domain.network.genres.get.repository.AnimeGenresRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeGenresRepositoryModule = module {
    singleOf(constructor = ::AnimeGenresRepositoryImpl) { bind<AnimeGenresRepository>() }
}
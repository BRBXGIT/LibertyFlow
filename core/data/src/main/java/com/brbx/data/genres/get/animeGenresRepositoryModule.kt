package com.brbx.data.genres.get

import com.brbx.domain.genres.get.repository.AnimeGenresRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeGenresRepositoryModule = module {
    singleOf(constructor = ::AnimeGenresRepositoryImpl) { bind<AnimeGenresRepository>() }
}
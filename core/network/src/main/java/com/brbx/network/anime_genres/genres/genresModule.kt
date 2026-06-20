package com.brbx.network.anime_genres.genres

import com.brbx.network.anime_genres.genres.api.AnimeGenresApi
import com.brbx.network.anime_genres.genres.api.AnimeGenresApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val genresModule = module {
    singleOf(constructor = ::AnimeGenresApiImpl) { bind<AnimeGenresApi>() }
}
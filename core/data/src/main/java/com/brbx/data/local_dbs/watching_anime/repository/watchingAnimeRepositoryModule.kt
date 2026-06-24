package com.brbx.data.local_dbs.watching_anime.repository

import com.brbx.data.local_dbs.watching_anime.repository.anime.watchingAnimeModule
import com.brbx.data.local_dbs.watching_anime.repository.episodes.watchedEpisodesModule
import org.koin.dsl.module

internal val watchingAnimeRepositoryModule = module {
    includes(
        watchingAnimeModule,
        watchedEpisodesModule,
    )
}
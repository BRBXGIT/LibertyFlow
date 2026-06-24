package com.brbx.domain.local_dbs.watching_anime.use_case

import com.brbx.domain.local_dbs.watching_anime.use_case.anime.watchingAnimeModule
import com.brbx.domain.local_dbs.watching_anime.use_case.episodes.watchedEpisodesModule
import org.koin.dsl.module

internal val watchingAnimeUseCaseModule = module {
    includes(
        watchingAnimeModule,
        watchedEpisodesModule,
    )
}
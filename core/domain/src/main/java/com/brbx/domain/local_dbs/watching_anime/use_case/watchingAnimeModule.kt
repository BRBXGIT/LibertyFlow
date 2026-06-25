package com.brbx.domain.local_dbs.watching_anime.use_case

import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.watchingAnimeUseCaseModule
import com.brbx.domain.local_dbs.watching_anime.use_case.episodes.use_case.watchedEpisodesUseCaseModule
import org.koin.dsl.module

internal val watchingAnimeModule = module {
    includes(
        watchingAnimeUseCaseModule,
        watchedEpisodesUseCaseModule,
    )
}
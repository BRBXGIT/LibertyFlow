package com.brbx.data.local_dbs

import com.brbx.data.local_dbs.watching_anime.repository.watchingAnimeRepositoryModule
import org.koin.dsl.module

internal val localDbsModule = module {
    includes(
        watchingAnimeRepositoryModule,
    )
}
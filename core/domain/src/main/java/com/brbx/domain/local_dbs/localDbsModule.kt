package com.brbx.domain.local_dbs

import com.brbx.domain.local_dbs.watching_anime.use_case.watchingAnimeModule
import org.koin.dsl.module

internal val localDbsModule = module {
    includes(
        watchingAnimeModule,
    )
}
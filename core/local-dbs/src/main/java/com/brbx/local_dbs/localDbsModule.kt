package com.brbx.local_dbs

import com.brbx.local_dbs.watched_episodes.watchedEpisodesModule
import org.koin.dsl.module

val localDbsModule = module {
    includes(
        watchedEpisodesModule,
    )
}
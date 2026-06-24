package com.brbx.local_dbs

import com.brbx.local_dbs.watch_history.watchHistoryModule
import org.koin.dsl.module

val localDbsModule = module {
    includes(
        watchHistoryModule,
    )
}
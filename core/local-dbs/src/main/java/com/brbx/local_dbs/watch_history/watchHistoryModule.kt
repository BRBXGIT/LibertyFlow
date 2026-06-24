package com.brbx.local_dbs.watch_history

import androidx.room.Room
import com.brbx.local_dbs.watch_history.db.db.WatchHistoryDb
import com.brbx.local_dbs.watch_history.provider.WatchHistoryDaoProvider
import com.brbx.local_dbs.watch_history.provider.WatchHistoryDaoProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val watchHistoryModule = module {
    single<WatchHistoryDb> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = WatchHistoryDb::class.java,
            name = "EpisodesDb"
        ).build()
    }
    singleOf(constructor = ::WatchHistoryDaoProviderImpl) { bind<WatchHistoryDaoProvider>() }
}
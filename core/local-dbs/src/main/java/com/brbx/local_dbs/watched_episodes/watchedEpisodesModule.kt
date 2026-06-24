package com.brbx.local_dbs.watched_episodes

import androidx.room.Room
import com.brbx.local_dbs.watched_episodes.db.EpisodesDb
import com.brbx.local_dbs.watched_episodes.provider.EpisodesDaoProvider
import com.brbx.local_dbs.watched_episodes.provider.EpisodesDaoProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val watchedEpisodesModule = module {
    single<EpisodesDb> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = EpisodesDb::class.java,
            name = "EpisodesDb"
        ).build()
    }
    singleOf(constructor = ::EpisodesDaoProviderImpl) { bind<EpisodesDaoProvider>() }
}
package com.brbx.data.local_dbs.watching_anime.repository.episodes

import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesReader
import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesWriter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val watchedEpisodesModule = module {
    singleOf(constructor = ::WatchedEpisodesReaderImpl) { bind<WatchedEpisodesReader>() }
    singleOf(constructor = ::WatchedEpisodesWriterImpl) { bind<WatchedEpisodesWriter>() }
}
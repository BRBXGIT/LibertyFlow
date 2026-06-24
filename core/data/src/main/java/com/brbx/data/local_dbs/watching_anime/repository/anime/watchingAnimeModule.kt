package com.brbx.data.local_dbs.watching_anime.repository.anime

import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeReader
import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeWriter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val watchingAnimeModule = module {
    singleOf(constructor = ::WatchingAnimeReaderImpl) { bind<WatchingAnimeReader>() }
    singleOf(constructor = ::WatchingAnimeWriterImpl) { bind<WatchingAnimeWriter>() }
}
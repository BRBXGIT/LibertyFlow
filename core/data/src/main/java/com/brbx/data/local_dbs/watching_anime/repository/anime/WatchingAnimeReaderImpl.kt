package com.brbx.data.local_dbs.watching_anime.repository.anime

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeReader
import com.brbx.local_dbs.watch_history.db.model.AnimeEntity
import com.brbx.local_dbs.watch_history.provider.WatchHistoryDaoProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class WatchingAnimeReaderImpl(
    private val dbDaoProvider: WatchHistoryDaoProvider,
) : WatchingAnimeReader {

    override fun getAnime(): Flow<List<DomainWatchingAnime>> =
        dbDaoProvider.animeDao.getAnime().map { list -> list.map { it.toDomain() } }

    private fun AnimeEntity.toDomain(): DomainWatchingAnime =
        DomainWatchingAnime(this.animeId, this.title)
}
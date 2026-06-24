package com.brbx.data.local_dbs.watching_anime.repository.anime

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeWriter
import com.brbx.local_dbs.watch_history.db.model.AnimeEntity
import com.brbx.local_dbs.watch_history.provider.WatchHistoryDaoProvider

class WatchingAnimeWriterImpl(
    private val dbDaoProvider: WatchHistoryDaoProvider,
) : WatchingAnimeWriter {

    override suspend fun insertAnime(anime: DomainWatchingAnime) {
        dbDaoProvider.animeDao.insertAnime(anime.toData())
    }

    private fun DomainWatchingAnime.toData(): AnimeEntity =
        AnimeEntity(
            animeId = this.id,
            title = this.title,
        )
}
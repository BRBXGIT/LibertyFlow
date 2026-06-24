package com.brbx.local_dbs.watch_history.provider

import com.brbx.local_dbs.watch_history.db.dao.AnimeDao
import com.brbx.local_dbs.watch_history.db.dao.EpisodesDao
import com.brbx.local_dbs.watch_history.db.db.WatchHistoryDb

internal class WatchHistoryDaoProviderImpl(db: WatchHistoryDb) : WatchHistoryDaoProvider {

    override val episodesDao: EpisodesDao by lazy { db.episodesDao() }
    override val animeDao: AnimeDao by lazy { db.animeDao() }
}
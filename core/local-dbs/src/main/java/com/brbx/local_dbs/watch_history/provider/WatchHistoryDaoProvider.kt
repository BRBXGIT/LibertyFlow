package com.brbx.local_dbs.watch_history.provider

import com.brbx.local_dbs.watch_history.db.dao.AnimeDao
import com.brbx.local_dbs.watch_history.db.dao.EpisodesDao

interface WatchHistoryDaoProvider {

    val episodesDao: EpisodesDao
    val animeDao: AnimeDao
}
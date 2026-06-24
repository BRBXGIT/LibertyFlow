package com.brbx.local_dbs.watch_history.db.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brbx.local_dbs.watch_history.db.dao.AnimeDao
import com.brbx.local_dbs.watch_history.db.dao.EpisodesDao
import com.brbx.local_dbs.watch_history.db.model.AnimeEntity
import com.brbx.local_dbs.watch_history.db.model.EpisodeEntity

@Database(
    entities = [
        AnimeEntity::class,
        EpisodeEntity::class,
    ],
    version = 1,
)
abstract class WatchHistoryDb : RoomDatabase() {

    abstract fun episodesDao(): EpisodesDao
    abstract fun animeDao(): AnimeDao
}
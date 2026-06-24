package com.brbx.local_dbs.watched_episodes.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AnimeEntity::class,
        EpisodeEntity::class,
    ],
    version = 1,
)
abstract class EpisodesDb : RoomDatabase() {

    abstract fun episodesDao(): EpisodesDao
}
package com.example.local.watched_eps_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AnimeEntity::class, EpisodeEntity::class],
    version = 1
)
abstract class WatchedEpsDb: RoomDatabase() {
    abstract fun watchedEpsDao(): WatchedEpsDao
}
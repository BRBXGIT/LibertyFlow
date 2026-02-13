package com.example.local.watched_eps_db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The main database instance for tracking watched episodes.
 * * This class serves as the primary access point to the persisted data,
 * providing access to [WatchedEpsDao].
 *
 * Database Schema:
 * - [AnimeEntity]: Stores unique anime titles.
 * - [EpisodeEntity]: Stores progress (watched episodes) linked to anime titles.
 */
@Database(
    entities = [
        AnimeEntity::class,
        EpisodeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WatchedEpsDb : RoomDatabase() {

    /**
     * Provides access to the Data Access Object (DAO) for watched episodes.
     * @return An instance of [WatchedEpsDao].
     */
    abstract fun watchedEpsDao(): WatchedEpsDao
}
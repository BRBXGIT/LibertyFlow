package com.example.local.watched_eps_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing watched anime episodes and title records.
 */
@Dao
interface WatchedEpsDao {

    /**
     * Inserts a new anime title into the database.
     * If the anime already exists, the operation is ignored.
     *
     * @param anime The [AnimeEntity] to be persisted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTitle(anime: AnimeEntity)

    /**
     * Records a specific episode as "watched" by inserting it into the database.
     * Uses [OnConflictStrategy.IGNORE] to prevent errors if the episode is already recorded.
     *
     * @param episode The [EpisodeEntity] containing animeId and episodeIndex.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchedEpisode(episode: EpisodeEntity)

    /**
     * Retrieves a reactive stream of watched episode indices for a specific anime.
     * * @param animeId The unique identifier of the anime.
     * @return A [Flow] emitting a list of episode indices whenever the data changes.
     */
    @Query(" SELECT episodeIndex FROM EpisodeEntity WHERE animeId = :animeId")
    fun getWatchedEpisodes(animeId: Int): Flow<List<Int>>
}
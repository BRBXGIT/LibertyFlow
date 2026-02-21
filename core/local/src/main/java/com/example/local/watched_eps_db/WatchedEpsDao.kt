package com.example.local.watched_eps_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
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
     * Uses [Upsert] to prevent errors if the episode is already recorded.
     *
     * @param episode The [EpisodeEntity] containing animeId and episodeIndex.
     */
    @Upsert
    suspend fun upsertWatchedEpisode(episode: EpisodeEntity)

    /**
     * Retrieves a reactive stream of watched episode indices for a specific anime.
     * @param animeId The unique identifier of the anime.
     * @return A [Flow] emitting a list of episode indices whenever the data changes.
     */
    @Query(" SELECT episodeIndex FROM EpisodeEntity WHERE animeId = :animeId")
    fun getWatchedEpisodes(animeId: Int): Flow<List<Int>>

    /**
     * Retrieves a reactive stream through the suspend function of watched episode last time for the specific anime.
     * @param animeId The unique identifier of the anime.
     * @param episodeIndex The index of episode.
     */
    @Query("SELECT lastPosition FROM EpisodeEntity WHERE animeId = :animeId AND episodeIndex = :episodeIndex")
    suspend fun getEpisodeProgress(animeId: Int, episodeIndex: Int): Long?
}
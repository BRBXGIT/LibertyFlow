package com.brbx.local_dbs.watched_episodes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Upsert
    suspend fun upsertWatchedEpisode(episode: EpisodeEntity)

    @Query(value = "SELECT episodeIndex FROM EpisodeEntity WHERE animeId = :animeId")
    fun getWatchedEpisodes(animeId: Int): Flow<List<Int>>

    @Query(
        value = "SELECT lastPosition " +
                "FROM EpisodeEntity " +
                "WHERE animeId = :animeId AND episodeIndex = :episodeIndex"
    )
    suspend fun getEpisodeProgress(animeId: Int, episodeIndex: Int): Long?
}
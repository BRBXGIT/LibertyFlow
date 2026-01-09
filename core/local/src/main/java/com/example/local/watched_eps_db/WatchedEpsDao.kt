package com.example.local.watched_eps_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedEpsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchedEpisode(episode: EpisodeEntity)

    @Query("SELECT episodeIndex FROM EpisodeEntity WHERE animeId = :animeId")
    fun getWatchedEpisodes(animeId: Int): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTitle(anime: AnimeEntity)
}
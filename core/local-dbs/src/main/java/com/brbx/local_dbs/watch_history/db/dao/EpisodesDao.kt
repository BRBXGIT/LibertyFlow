package com.brbx.local_dbs.watch_history.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.brbx.local_dbs.watch_history.db.model.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodesDao {

    @Query(value = "SELECT episodeIndex FROM EpisodeEntity WHERE animeId = :animeId")
    fun getWatchedEpisodesIndexes(animeId: Int): Flow<List<Int>>

    @Upsert
    suspend fun upsertWatchedEpisode(episode: EpisodeEntity)

    @Query(
        value = "SELECT lastPosition " +
                "FROM EpisodeEntity " +
                "WHERE animeId = :animeId AND episodeIndex = :episodeIndex"
    )
    suspend fun getEpisodeProgress(animeId: Int, episodeIndex: Int): Long?
}
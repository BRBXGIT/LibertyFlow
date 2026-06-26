package com.brbx.local_dbs.watch_history.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brbx.local_dbs.watch_history.db.model.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Query("SELECT * FROM animeentity")
    fun getAnime(): Flow<List<AnimeEntity>>
}
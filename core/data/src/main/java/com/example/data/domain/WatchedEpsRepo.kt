package com.example.data.domain

import kotlinx.coroutines.flow.Flow

interface WatchedEpsRepo {

    suspend fun upsertWatchedEpisode(animeId: Int, episodeIndex: Int, lastPosition: Long)

    fun getWatchedEpisodes(animeId: Int): Flow<List<Int>>

    suspend fun insertTitle(animeId: Int)

    suspend fun getEpisodeProgress(animeId: Int, episodeIndex: Int): Long
}
package com.example.data.domain

import kotlinx.coroutines.flow.Flow

interface WatchedEpsRepo {

    suspend fun insertWatchedEpisode(animeId: Int, episodeIndex: Int)

    fun getWatchedEpisodes(animeId: Int): Flow<List<Int>>

    suspend fun insertTitle(animeId: Int)
}
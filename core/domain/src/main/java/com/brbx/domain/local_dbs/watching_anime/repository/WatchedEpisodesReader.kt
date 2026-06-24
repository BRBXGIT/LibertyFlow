package com.brbx.domain.local_dbs.watching_anime.repository

import kotlinx.coroutines.flow.Flow

interface WatchedEpisodesReader {

    fun getEpisodesIndexes(animeId: Int): Flow<List<Int>>

    suspend fun getEpisodeProgress(animeId: Int, episodeIndex: Int): Long
}
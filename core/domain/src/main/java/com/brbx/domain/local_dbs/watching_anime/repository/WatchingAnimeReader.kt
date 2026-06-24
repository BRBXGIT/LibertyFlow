package com.brbx.domain.local_dbs.watching_anime.repository

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchingAnime
import kotlinx.coroutines.flow.Flow

interface WatchingAnimeReader {

    fun getAnime(): Flow<List<DomainWatchingAnime>>
}
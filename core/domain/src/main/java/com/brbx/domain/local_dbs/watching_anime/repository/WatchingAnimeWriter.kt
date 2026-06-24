package com.brbx.domain.local_dbs.watching_anime.repository

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchingAnime

interface WatchingAnimeWriter {

    suspend fun insertAnime(anime: DomainWatchingAnime)
}
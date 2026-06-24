package com.brbx.domain.local_dbs.watching_anime.repository

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchedEpisode

interface WatchedEpisodesWriter {

    suspend fun upsertEpisode(episode: DomainWatchedEpisode)
}
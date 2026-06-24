package com.brbx.domain.local_dbs.watching_anime.model

data class DomainWatchedEpisode(
    val animeId: Int,
    val episodeIndex: Int,
    val lastPosition: Long,
)
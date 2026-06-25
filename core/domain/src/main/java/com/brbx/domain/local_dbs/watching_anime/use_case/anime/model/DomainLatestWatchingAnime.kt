package com.brbx.domain.local_dbs.watching_anime.use_case.anime.model

data class DomainLatestWatchingAnime(
    val animeId: Int,
    val title: String,
    val lastEpisodeIndex: Int,
)

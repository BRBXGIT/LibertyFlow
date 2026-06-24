package com.brbx.domain.local_dbs.watching_anime.use_case.anime

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeWriter

class InsertWatchingAnimeUseCase(
    private val writer: WatchingAnimeWriter,
) {
    suspend operator fun invoke(animeId: Int, title: String) =
        writer.insertAnime(DomainWatchingAnime(animeId, title))
}
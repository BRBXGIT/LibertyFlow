package com.brbx.domain.local_dbs.watching_anime.use_case.anime

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeReader
import kotlinx.coroutines.flow.first

class GetLatestWatchingAnimeUseCase(
    private val reader: WatchingAnimeReader,
) {
    suspend operator fun invoke(): DomainWatchingAnime? =
        reader.getAnime().first().firstOrNull()
}
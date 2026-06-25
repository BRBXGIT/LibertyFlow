package com.brbx.domain.local_dbs.watching_anime.use_case.episodes.use_case

import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesReader
import kotlinx.coroutines.flow.Flow

class GetWatchedEpisodesIndexesUseCase(
    private val reader: WatchedEpisodesReader,
) {
    operator fun invoke(animeId: Int): Flow<List<Int>> =
        reader.getEpisodesIndexes(animeId)
}
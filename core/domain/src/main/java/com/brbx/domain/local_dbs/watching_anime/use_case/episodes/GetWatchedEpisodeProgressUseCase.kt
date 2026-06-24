package com.brbx.domain.local_dbs.watching_anime.use_case.episodes

import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesReader

class GetWatchedEpisodeProgressUseCase(
    private val reader: WatchedEpisodesReader,
) {
    suspend operator fun invoke(
        animeId: Int,
        episodeIndex: Int,
    ): Long = reader.getEpisodeProgress(animeId, episodeIndex)
}
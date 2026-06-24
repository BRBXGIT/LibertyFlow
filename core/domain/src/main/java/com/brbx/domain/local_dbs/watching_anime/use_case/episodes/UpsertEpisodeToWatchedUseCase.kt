package com.brbx.domain.local_dbs.watching_anime.use_case.episodes

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchedEpisode
import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesWriter

class UpsertEpisodeToWatchedUseCase(
    private val writer: WatchedEpisodesWriter,
) {
    suspend operator fun invoke(
        animeId: Int,
        episodeIndex: Int,
        lastPosition: Long,
    ) {
        val model = DomainWatchedEpisode(
            animeId = animeId,
            episodeIndex = episodeIndex,
            lastPosition = lastPosition,
        )
        writer.upsertEpisode(model)
    }
}
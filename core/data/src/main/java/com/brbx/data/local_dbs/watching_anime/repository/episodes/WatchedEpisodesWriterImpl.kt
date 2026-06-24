package com.brbx.data.local_dbs.watching_anime.repository.episodes

import com.brbx.domain.local_dbs.watching_anime.model.DomainWatchedEpisode
import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesWriter
import com.brbx.local_dbs.watch_history.db.model.EpisodeEntity
import com.brbx.local_dbs.watch_history.provider.WatchHistoryDaoProvider

internal class WatchedEpisodesWriterImpl(
    private val dbDaoProvider: WatchHistoryDaoProvider,
) : WatchedEpisodesWriter {

    override suspend fun upsertEpisode(episode: DomainWatchedEpisode) {
        dbDaoProvider.episodesDao.upsertWatchedEpisode(episode.toData())
    }

    private fun DomainWatchedEpisode.toData(): EpisodeEntity =
        EpisodeEntity(
            animeId = this.animeId,
            episodeIndex = this.episodeIndex,
            lastPosition = this.lastPosition,
        )
}
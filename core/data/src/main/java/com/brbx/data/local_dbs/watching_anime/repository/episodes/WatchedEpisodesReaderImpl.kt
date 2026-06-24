package com.brbx.data.local_dbs.watching_anime.repository.episodes

import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesReader
import com.brbx.local_dbs.watch_history.provider.WatchHistoryDaoProvider
import kotlinx.coroutines.flow.Flow

internal class WatchedEpisodesReaderImpl(
    private val dbDaoProvider: WatchHistoryDaoProvider,
) : WatchedEpisodesReader {

    override fun getEpisodesIndexes(animeId: Int): Flow<List<Int>> =
        dbDaoProvider.episodesDao.getWatchedEpisodesIndexes(animeId)

    override suspend fun getEpisodeProgress(
        animeId: Int,
        episodeIndex: Int
    ): Long =
        dbDaoProvider.episodesDao.getEpisodeProgress(animeId, episodeIndex) ?: StartEpisodeProgress

    private companion object {
        const val StartEpisodeProgress = 0L
    }
}
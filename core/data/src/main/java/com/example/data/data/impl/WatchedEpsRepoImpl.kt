package com.example.data.data.impl

import com.example.data.domain.WatchedEpsRepo
import com.example.local.watched_eps_db.AnimeEntity
import com.example.local.watched_eps_db.EpisodeEntity
import com.example.local.watched_eps_db.WatchedEpsDao
import javax.inject.Inject

/**
 * Implementation of [WatchedEpsRepo] that manages local history of watched episodes.
 * Acts as a bridge to the [WatchedEpsDao] for Room database operations.
 */
class WatchedEpsRepoImpl @Inject constructor(
    private val dao: WatchedEpsDao
): WatchedEpsRepo {

    /**
     * Initializes a history entry for an anime title by its [animeId].
     */
    override suspend fun insertTitle(animeId: Int) =
        dao.insertTitle(AnimeEntity(animeId))

    /**
     * Records a specific episode as watched for the given [animeId].
     */
    override suspend fun upsertWatchedEpisode(animeId: Int, episodeIndex: Int, lastPosition: Long) =
        dao.upsertWatchedEpisode(EpisodeEntity(animeId, episodeIndex, lastPosition))

    /**
     * Returns a Flow containing the indices of all watched episodes for a specific anime.
     */
    override fun getWatchedEpisodes(animeId: Int) =
        dao.getWatchedEpisodes(animeId)

    /**
     * Returns a Flow through the suspend function the last position of current episode for a specific anime.
     */
    override suspend fun getEpisodeProgress(animeId: Int, episodeIndex: Int): Long =
        dao.getEpisodeProgress(animeId, episodeIndex) ?: 0L
}
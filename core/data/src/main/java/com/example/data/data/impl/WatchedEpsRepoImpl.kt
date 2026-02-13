package com.example.data.data.impl

import com.example.data.domain.WatchedEpsRepo
import com.example.local.watched_eps_db.AnimeEntity
import com.example.local.watched_eps_db.EpisodeEntity
import com.example.local.watched_eps_db.WatchedEpsDao
import javax.inject.Inject

class WatchedEpsRepoImpl @Inject constructor(
    private val dao: WatchedEpsDao
): WatchedEpsRepo {

    override suspend fun insertTitle(animeId: Int) =
        dao.insertTitle(AnimeEntity(animeId))

    override suspend fun insertWatchedEpisode(animeId: Int, episodeIndex: Int) =
        dao.insertWatchedEpisode(EpisodeEntity(animeId, episodeIndex))

    override fun getWatchedEpisodes(animeId: Int) =
        dao.getWatchedEpisodes(animeId)
}
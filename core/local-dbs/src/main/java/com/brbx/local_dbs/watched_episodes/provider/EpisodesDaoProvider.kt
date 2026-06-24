package com.brbx.local_dbs.watched_episodes.provider

import com.brbx.local_dbs.watched_episodes.db.EpisodesDao

interface EpisodesDaoProvider {

    val episodesDao: EpisodesDao
}
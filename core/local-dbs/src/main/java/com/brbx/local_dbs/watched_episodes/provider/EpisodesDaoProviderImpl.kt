package com.brbx.local_dbs.watched_episodes.provider

import com.brbx.local_dbs.watched_episodes.db.EpisodesDb

internal class EpisodesDaoProviderImpl(db: EpisodesDb) : EpisodesDaoProvider {

    override val episodesDao by lazy { db.episodesDao() }
}
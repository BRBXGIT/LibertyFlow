package com.brbx.data.network.releases.by_id.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.common.toDomain
import com.brbx.domain.network.model.common.PublishStatus
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Type
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.by_id.model.DomainAnimeItemById
import com.brbx.domain.network.releases.by_id.repository.AnimeReleaseByIdRepository
import com.brbx.network.anime_releases.by_id.api.AnimeReleasesByIdApi
import com.brbx.network.anime_releases.by_id.model.AnimeItemById

internal class AnimeReleaseByIdRepositoryImpl(
    private val api: AnimeReleasesByIdApi,
    private val executor: ApiCallExecutor,
) : AnimeReleaseByIdRepository {

    override suspend fun getRelease(id: Int): DomainRequestResult<DomainAnimeItemById> =
        executor.execute(mapper = { it.toDomain() }) { api.getRelease(id) }

    private fun AnimeItemById.toDomain(): DomainAnimeItemById =
        DomainAnimeItemById(
            publishStatus = PublishStatus.fromData(isOngoing = this.isOngoing),
            description = this.description,
            episodes = this.episodes.map { it.toDomain() },
            genres = this.genres.map { it.toDomain() },
            id = this.id,
            members = this.members.map { it.toDomain() },
            name = this.name.toDomain(),
            poster = this.poster.toDomain(),
            season = Season.fromData(value = this.season.description),
            torrents = this.torrents.map { it.toDomain() },
            type = Type.fromData(value = this.type.description),
            year = this.year,
        )

    private fun AnimeItemById.Episode.toDomain(): DomainAnimeItemById.DomainEpisode =
        DomainAnimeItemById.DomainEpisode(
            hls1080 = this.hls1080,
            hls480 = this.hls480,
            hls720 = this.hls720,
            ending = this.ending.toDomain(),
            opening = this.opening.toDomain(),
            name = this.name,
        )

    private fun AnimeItemById.Episode.Segment.toDomain():
            DomainAnimeItemById.DomainEpisode.DomainSegment =
        DomainAnimeItemById.DomainEpisode.DomainSegment(
            start = this.start,
            stop = this.stop,
        )

    private fun AnimeItemById.Member.toDomain(): DomainAnimeItemById.DomainMember =
        DomainAnimeItemById.DomainMember(
            nickname = this.nickname,
            role = this.role.description,
        )

    private fun AnimeItemById.Torrent.toDomain(): DomainAnimeItemById.DomainTorrent =
        DomainAnimeItemById.DomainTorrent(
            filename = this.filename,
            leechers = this.leechers,
            magnet = this.magnet,
            seeders = this.seeders,
            size = this.size,
        )
}
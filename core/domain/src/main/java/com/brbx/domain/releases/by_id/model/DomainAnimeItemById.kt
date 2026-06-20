package com.brbx.domain.releases.by_id.model

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.common.PublishStatus
import com.brbx.domain.model.common.Season
import com.brbx.domain.model.common.Type
import com.brbx.domain.model.response.common.DomainName
import com.brbx.domain.model.response.common.DomainPoster

data class DomainAnimeItemById(
    val publishStatus: PublishStatus,
    val description: String,
    val episodes: List<Episode>,
    val genres: List<DomainGenre>,
    val id: Int,
    val members: List<Member>,
    val name: DomainName,
    val poster: DomainPoster,
    val season: Season,
    val torrents: List<Torrent>,
    val type: Type,
    val year: Int
) {
    data class Episode(
        val hls1080: String?,
        val hls480: String,
        val hls720: String,
        val ending: Segment,
        val name: String,
        val opening: Segment,
    ) {
        data class Segment(
            val start: Int?,
            val stop: Int?,
        )
    }

    data class Member(
        val nickname: String,
        val role: Role,
    ) {
        @JvmInline
        value class Role(val description: String)
    }

    data class Torrent(
        val filename: String,
        val leechers: Int,
        val magnet: String,
        val seeders: Int,
        val size: Long,
    )
}
package com.brbx.network.anime_releases.by_id.model

import com.brbx.network.base.model.response.common.Genre
import com.brbx.network.base.model.response.common.Name
import com.brbx.network.base.model.response.common.Poster
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeItemById(
    @SerialName("is_ongoing") val isOngoing: Boolean,
    val description: String,
    val episodes: List<Episode>,
    val genres: List<Genre.Simple>,
    val id: Int,
    val members: List<Member>,
    val name: Name,
    val poster: Poster,
    val season: Season,
    val torrents: List<Torrent>,
    val type: Type,
    val year: Int
) {
    @Serializable
    data class Episode(
        @SerialName("hls_1080") val hls1080: String?,
        @SerialName("hls_480") val hls480: String,
        @SerialName("hls_720") val hls720: String,
        val ending: Segment,
        val name: String,
        val opening: Segment,
    ) {
        @Serializable
        data class Segment(
            val start: Int?,
            val stop: Int?,
        )
    }

    @Serializable
    data class Member(
        val nickname: String,
        val role: Role,
    ) {
        @JvmInline
        @Serializable
        value class Role(val description: String)
    }

    @JvmInline
    @Serializable
    value class Season(val description: String)

    @Serializable
    data class Torrent(
        val filename: String,
        val leechers: Int,
        val magnet: String,
        val seeders: Int,
        val size: Long,
    )

    @JvmInline
    @Serializable
    value class Type(val description: String)
}
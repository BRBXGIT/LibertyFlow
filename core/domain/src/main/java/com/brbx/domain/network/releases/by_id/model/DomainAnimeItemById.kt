package com.brbx.domain.network.releases.by_id.model

import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.common.PublishStatus
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Type
import com.brbx.domain.network.model.response.common.DomainName
import com.brbx.domain.network.model.response.common.DomainPoster
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

data class DomainAnimeItemById(
    val publishStatus: PublishStatus,
    val description: String,
    val episodes: List<DomainEpisode>,
    val genres: List<DomainGenre>,
    val id: Int,
    val members: List<DomainMember>,
    val name: DomainName,
    val poster: DomainPoster,
    val season: Season,
    val torrents: List<DomainTorrent>,
    val type: Type,
    val year: Int,
) {
    data class DomainEpisode(
        val hls1080: String?,
        val hls480: String,
        val hls720: String,
        val ending: DomainSegment,
        val name: String,
        val opening: DomainSegment,
    ) {
        data class DomainSegment(
            val start: Int?,
            val stop: Int?,
        )
    }

    data class DomainMember(
        val nickname: String,
        val role: String,
    )

    data class DomainTorrent(
        val filename: String,
        val leechers: Int,
        val magnet: String,
        val seeders: Int,
        val size: Long,
    ) {
        fun formatSize(sizeInBytes: Long): String {
            if (sizeInBytes <= 0) return "0 B"

            val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")

            val digitGroups = (ln(x = sizeInBytes.toDouble()) / ln(x = 1024.0)).toInt()

            val value = sizeInBytes / 1024.0.pow(x = digitGroups.toDouble())

            return String.format(Locale.US, format = "%.2f %s", value, units[digitGroups])
        }
    }
}
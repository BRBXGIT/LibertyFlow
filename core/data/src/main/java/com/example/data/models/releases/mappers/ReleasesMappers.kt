package com.example.data.models.releases.mappers

import com.example.data.models.common.mappers.toGenre
import com.example.data.models.common.mappers.toLabelRes
import com.example.data.models.common.mappers.toPoster
import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.releases.anime_details.Ending
import com.example.data.models.releases.anime_details.Episode
import com.example.data.models.releases.anime_details.Member
import com.example.data.models.releases.anime_details.Opening
import com.example.data.models.releases.anime_details.Torrent
import com.example.data.models.releases.anime_id.AnimeId
import com.example.network.releases.models.anime_details_item_response.AnimeDetailsItemDto
import com.example.network.releases.models.anime_details_item_response.EndingDto
import com.example.network.releases.models.anime_details_item_response.EpisodeDto
import com.example.network.releases.models.anime_details_item_response.MemberDto
import com.example.network.releases.models.anime_details_item_response.OpeningDto
import com.example.network.releases.models.anime_details_item_response.TorrentDto
import com.example.network.releases.models.anime_id_item_response.AnimeIdItemDto

internal fun AnimeDetailsItemDto.toAnimeDetails(): AnimeDetails {
    return AnimeDetails(
        id = id,
        description = description,
        episodes = episodes.map { it.toEpisode() },
        genres = genres.map { it.toGenre() },
        isOngoing = isOngoing,
        members = members.map { it.toMember() },
        name = nameDto.toLabelRes(),
        poster = posterDto.toPoster(),
        season = seasonDto.description,
        torrents = torrents.map { it.toTorrent() },
        type = typeDto.description,
        year = year
    )
}

internal fun EpisodeDto.toEpisode(): Episode {
    return Episode(
        opening = openingDto.toOpening(),
        ending = endingDto.toEnding(),
        hls1080 = hls1080,
        hls480 = hls480,
        hls720 = hls720,
        name = name
    )
}

internal fun OpeningDto.toOpening(): Opening {
    return Opening(
        start = start,
        end = stop
    )
}

internal fun EndingDto.toEnding(): Ending {
    return Ending(
        start = start,
        end = stop
    )
}

internal fun MemberDto.toMember(): Member {
    return Member(
        name = nickname,
        role = roleDto.description
    )
}

internal fun TorrentDto.toTorrent(): Torrent {
    return Torrent(
        filename = filename,
        leechers = leechers,
        seeders = seeders,
        size = (size / 8 / (1024 * 1024)).toInt(), // Convert from bits to mb
        magnet = magnet
    )
}
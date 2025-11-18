package com.example.data.models.releases.mappers

import com.example.data.models.common.mappers.toUiGenre
import com.example.data.models.common.mappers.toUiName
import com.example.data.models.common.mappers.toUiPoster
import com.example.data.models.releases.anime_details.UiAnimeDetails
import com.example.data.models.releases.anime_details.UiEnding
import com.example.data.models.releases.anime_details.UiEpisode
import com.example.data.models.releases.anime_details.UiMember
import com.example.data.models.releases.anime_details.UiOpening
import com.example.data.models.releases.anime_details.UiTorrent
import com.example.data.models.releases.anime_id.UiAnimeId
import com.example.network.releases.models.anime_details_item_response.AnimeDetailsItem
import com.example.network.releases.models.anime_details_item_response.Ending
import com.example.network.releases.models.anime_details_item_response.Episode
import com.example.network.releases.models.anime_details_item_response.Member
import com.example.network.releases.models.anime_details_item_response.Opening
import com.example.network.releases.models.anime_details_item_response.Torrent
import com.example.network.releases.models.anime_id_item_response.AnimeIdItem

internal fun AnimeIdItem.toUiAnimeId() = UiAnimeId(id)

internal fun AnimeDetailsItem.toUiAnimeDetails(): UiAnimeDetails {
    return UiAnimeDetails(
        id = id,
        description = description,
        episodes = episodes.map { it.toUiEpisode() },
        genres = genres.map { it.toUiGenre() },
        isOngoing = isOngoing,
        members = members.map { it.toUiMember() },
        name = name.toUiName(),
        poster = poster.toUiPoster(),
        season = season.description,
        torrents = torrents.map { it.toUiTorrent() },
        type = type.description,
        year = year
    )
}

internal fun Episode.toUiEpisode(): UiEpisode {
    return UiEpisode(
        opening = opening.toUiOpening(),
        ending = ending.toUiEnding(),
        hls1080 = hls1080,
        hls480 = hls480,
        hls720 = hls720,
        name = name
    )
}

internal fun Opening.toUiOpening(): UiOpening {
    return UiOpening(
        start = start,
        end = stop
    )
}

internal fun Ending.toUiEnding(): UiEnding {
    return UiEnding(
        start = start,
        end = stop
    )
}

internal fun Member.toUiMember(): UiMember {
    return UiMember(
        name = nickname,
        role = role.description
    )
}

internal fun Torrent.toUiTorrent(): UiTorrent {
    return UiTorrent(
        filename = filename,
        leechers = leechers,
        seeders = seeders,
        size = (size / 8 / (1024 * 1024)).toInt(), // Convert from bits to mb
        magnet = magnet
    )
}
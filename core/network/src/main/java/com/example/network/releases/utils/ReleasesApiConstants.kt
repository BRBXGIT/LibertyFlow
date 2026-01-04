package com.example.network.releases.utils

internal object ReleasesApiConstants {
    const val RANDOM_ANIME_INCLUDE = "id"
    const val RANDOM_ANIME_LIMIT = 1

    const val CURRENT_ANIME_INCLUDE = "poster.optimized," +
            "name," +
            "season.description," +
            "type.description," +
            "year," +
            "is_ongoing," +
            "description," +
            "genres.name," +
            "genres.id," +
            "members.role.description,members.nickname," +
            "episodes.opening,episodes.ending,episodes.hls_480,episodes.hls_720,episodes.hls_1080,episodes.name," +
            "torrents.filename,torrents.leechers,torrents.seeders,torrents.size,torrents.magnet," +
            "id"
}
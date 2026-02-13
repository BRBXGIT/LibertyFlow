package com.example.network.releases.utils

/**
 * Configuration constants for the Releases API.
 * Marked as 'internal' to restrict visibility to the networking module.
 */
internal object ReleasesApiConstants {
    const val RANDOM_ANIME_INCLUDE = "id"
    const val RANDOM_ANIME_LIMIT = 1

    /** * Comprehensive include string for the Detail Screen.
     * Fetches:
     * - Media: Posters, Episodes (with HLS streams), Torrents (with magnets)
     * - Metadata: Seasons, Types, Ongoing status, Genres
     * - Cast: Members and their roles
     */
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
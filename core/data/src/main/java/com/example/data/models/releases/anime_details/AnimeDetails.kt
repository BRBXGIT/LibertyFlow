package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.Genre
import com.example.data.models.common.common.Name
import com.example.data.models.common.common.Poster

/**
 * Detailed information for a specific anime title.
 * * This model aggregates metadata, media assets, localized naming, and
 * structural data (episodes/torrents) required to populate a full
 * information screen.
 *
 * @property id The unique identifier for the anime.
 * @property description A synopsis or summary of the plot. Can be null if unavailable.
 * @property episodes The list of available [Episode] objects.
 * @property genres Categorical tags associated with the anime.
 * @property isOngoing Indicates if the series is currently airing.
 * @property members The production staff or voice cast associated with the title.
 * @property name Localized and alternative titles.
 * @property poster Visual assets for the title in various resolutions.
 * @property season The release season (e.g., "Summer", "Winter").
 * @property torrents A list of available [Torrent] files for high-quality downloads.
 * @property type The format of the anime (e.g., "TV", "Movie", "OVA").
 * @property year The primary release year.
 */
@Immutable
data class AnimeDetails(
    val id: Int,
    val description: String?,
    val episodes: List<Episode>,
    val genres: List<Genre>,
    val isOngoing: Boolean,
    val members: List<Member>,
    val name: Name,
    val poster: Poster,
    val season: String,
    val torrents: List<Torrent>,
    val type: String,
    val year: Int
)
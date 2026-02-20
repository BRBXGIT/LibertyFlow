package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable

/**
 * Represents metadata for a peer-to-peer download (Torrent).
 *
 * This model provides the necessary information for tracking swarm health
 * (seeders/leechers) and initiating downloads via magnet links.
 *
 * @property filename The full original name of the torrent file.
 * @property leechers The number of peers currently downloading but not yet seeding.
 * @property seeders The number of peers currently providing the full file.
 * @property size The total file size in bytes.
 * @property magnet The URI magnet link used to initiate the download in a torrent client.
 */
@Immutable
data class Torrent(
    val filename: String = "",
    val leechers: Int = 0,
    val seeders: Int = 0,
    val size: Int = 0,
    val magnet: String = ""
) {
    /**
     * Extracts and joins all content found within square brackets `[...]` from the [filename].
     * * This is useful for displaying tags (e.g., "SubsPlease 1080p HVD") while
     * hiding the messy file extensions or full paths.
     *
     * @return A space-separated string of the bracketed metadata tags.
     */
    fun croppedFileName(): String {
        val value = "\\[(.*?)]".toRegex()
            .findAll(filename)
            .joinToString(" ") { it.groupValues[1] }

        return value
    }
}

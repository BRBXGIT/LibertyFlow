package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a torrent file for an anime release.
 * Contains metadata for peer-to-peer distribution and magnet URI links.
 *
 * @property filename The name of the .torrent file.
 * @property leechers The number of active peers currently downloading the file.
 * @property seeders The number of active peers currently uploading the complete file.
 * @property size The total size of the torrent payload in bytes.
 * @property magnet The Magnet URI for direct peer-to-peer connection.
 */
data class TorrentDto(
    @field:SerializedName(FIELD_FILENAME)
    val filename: String,

    @field:SerializedName(FIELD_LEECHERS)
    val leechers: Int,

    @field:SerializedName(FIELD_SEEDERS)
    val seeders: Int,

    @field:SerializedName(FIELD_SIZE)
    val size: Long,

    @field:SerializedName(FIELD_MAGNET)
    val magnet: String
) {
    companion object Fields {
        private const val FIELD_FILENAME = "filename"
        private const val FIELD_LEECHERS = "leechers"
        private const val FIELD_SEEDERS = "seeders"
        private const val FIELD_SIZE = "size"
        private const val FIELD_MAGNET = "magnet"
    }
}
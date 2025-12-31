package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable

@Immutable
data class UiTorrent(
    val filename: String,
    val leechers: Int,
    val seeders: Int,
    val size: Int,
    val magnet: String
) {
    fun croppedFileName(): String {
        val value = "\\[(.*?)]".toRegex()
            .findAll(filename)
            .joinToString(" ") { it.groupValues[1] }

        return value
    }
}

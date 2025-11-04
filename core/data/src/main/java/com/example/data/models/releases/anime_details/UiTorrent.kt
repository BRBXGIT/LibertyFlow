package com.example.data.models.releases.anime_details

data class UiTorrent(
    val filename: String,
    val leechers: Int,
    val seeders: Int,
    val size: Int,
    val magnet: String
)

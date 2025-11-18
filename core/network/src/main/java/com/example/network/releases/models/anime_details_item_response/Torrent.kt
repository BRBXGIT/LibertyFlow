package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

data class Torrent(
    @SerializedName("filename") val filename: String,
    @SerializedName("leechers") val leechers: Int,
    @SerializedName("seeders") val seeders: Int,
    @SerializedName("size") val size: Long,
    @SerializedName("magnet") val magnet: String
)
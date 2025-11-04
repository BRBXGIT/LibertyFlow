package com.example.data.models.releases.anime_details

data class UiEpisode(
    val opening: UiOpening,
    val ending: UiEnding,
    val hls1080: String?,
    val hls480: String,
    val hls720: String,
    val name: String?,
)
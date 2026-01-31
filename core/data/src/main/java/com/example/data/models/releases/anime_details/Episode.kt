package com.example.data.models.releases.anime_details

import androidx.compose.runtime.Immutable

@Immutable
data class Episode(
    val opening: Opening,
    val ending: Ending,
    val hls1080: String?,
    val hls480: String,
    val hls720: String?,
    val name: String?,
)
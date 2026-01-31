package com.example.data.models.releases.anime_details

import com.example.data.models.common.common.Genre
import com.example.data.models.common.common.Name
import com.example.data.models.common.common.Poster

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
package com.example.data.models.releases.anime_details

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster
import com.example.data.models.common.ui_anime_base.UiAnimeBase

data class UiAnimeDetails(
    override val id: Int,

    val description: String,
    val episodes: List<UiEpisode>,
    val genres: List<UiGenre>,
    val isOngoing: Boolean,
    val members: List<UiMember>,
    val name: UiName,
    val poster: UiPoster,
    val season: String,
    val torrents: List<UiTorrent>,
    val type: String,
    val year: Int
): UiAnimeBase
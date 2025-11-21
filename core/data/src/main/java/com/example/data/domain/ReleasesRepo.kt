package com.example.data.domain

import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.models.releases.anime_details.UiAnimeDetails
import com.example.data.models.releases.anime_id.UiAnimeId
import com.example.data.utils.remote.network_request.NetworkResult

interface ReleasesRepo {

    suspend fun getRandomAnime(): NetworkResult<UiAnimeId>

    suspend fun getAnime(id: Int): NetworkResult<UiAnimeDetails>
}
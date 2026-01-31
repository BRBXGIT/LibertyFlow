package com.example.data.domain

import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.releases.anime_id.AnimeId
import com.example.data.utils.remote.network_request.NetworkResult

interface ReleasesRepo {

    suspend fun getRandomAnime(): NetworkResult<AnimeId>

    suspend fun getAnime(id: Int): NetworkResult<AnimeDetails>
}
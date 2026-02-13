package com.example.data.data.impl

import com.example.data.domain.ReleasesRepo
import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.releases.anime_id.AnimeId
import com.example.data.models.releases.mappers.toAnimeDetails
import com.example.data.models.releases.mappers.toAnimeId
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.network.releases.api.ReleasesApi
import javax.inject.Inject

private const val ZERO = 0

class ReleasesRepoImpl @Inject constructor(
    private val releasesApi: ReleasesApi
): ReleasesRepo {

    override suspend fun getAnime(id: Int): NetworkResult<AnimeDetails> {
        return NetworkRequest.safeApiCall(
            call = { releasesApi.getAnime(id) },
            map = { it.toAnimeDetails() }
        )
    }

    override suspend fun getRandomAnime(): NetworkResult<AnimeId> {
        return NetworkRequest.safeApiCall(
            call = { releasesApi.getRandomAnime() },
            map = { it[ZERO].toAnimeId() } // First and the only item
        )
    }
}
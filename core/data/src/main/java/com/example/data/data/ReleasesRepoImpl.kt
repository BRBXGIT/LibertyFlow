package com.example.data.data

import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.mappers.toUiAnimeItem
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.models.releases.anime_details.UiAnimeDetails
import com.example.data.models.releases.anime_id.UiAnimeId
import com.example.data.models.releases.mappers.toUiAnimeDetails
import com.example.data.models.releases.mappers.toUiAnimeId
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.network.releases.api.ReleasesApi
import javax.inject.Inject

class ReleasesRepoImpl @Inject constructor(
    private val releasesApi: ReleasesApi
): ReleasesRepo {

    override suspend fun getAnime(id: Int): NetworkResult<UiAnimeDetails> {
        return NetworkRequest.safeApiCall(
            call = { releasesApi.getAnime(id) },
            map = { it.toUiAnimeDetails() }
        )
    }

    override suspend fun getRandomAnime(): NetworkResult<UiAnimeId> {
        return NetworkRequest.safeApiCall(
            call = { releasesApi.getRandomAnime() },
            map = { it[0].toUiAnimeId() } // First and the only item
        )
    }
}
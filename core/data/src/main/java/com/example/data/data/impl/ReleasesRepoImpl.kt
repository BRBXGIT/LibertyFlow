package com.example.data.data.impl

import com.example.data.domain.ReleasesRepo
import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.releases.anime_id.AnimeId
import com.example.data.models.releases.mappers.toAnimeDetails
import com.example.data.utils.network.network_request.NetworkRequest
import com.example.data.utils.network.network_request.NetworkResult
import com.example.network.releases.api.ReleasesApi
import com.example.network.releases.models.anime_id_item_response.AnimeIdItemDto
import javax.inject.Inject

private const val ZERO = 0

/**
 * Implementation of [ReleasesRepo] for fetching specific anime details and random releases.
 */
class ReleasesRepoImpl @Inject constructor(
    private val releasesApi: ReleasesApi
): ReleasesRepo {

    /**
     * Retrieves detailed information for a specific anime by [id].
     */
    override suspend fun getAnime(id: Int): NetworkResult<AnimeDetails> {
        return NetworkRequest.safeApiCall(
            call = { releasesApi.getAnime(id) },
            map = { it.toAnimeDetails() }
        )
    }

    /**
     * Fetches a single random anime identifier.
     * * The API returns a list and selects the first element.
     */
    override suspend fun getRandomAnime(): NetworkResult<AnimeId> {
        return NetworkRequest.safeApiCall(
            call = { releasesApi.getRandomAnime() },
            map = { it[ZERO].toAnimeId() } // First and the only item
        )
    }

    // --- Mappers ---
    fun AnimeIdItemDto.toAnimeId() = AnimeId(id)
}
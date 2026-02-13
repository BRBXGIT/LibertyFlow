package com.example.network.releases.api

import com.example.network.common.common_utils.CommonNetworkConstants
import com.example.network.releases.models.anime_details_item_response.AnimeDetailsItemDto
import com.example.network.releases.models.anime_id_item_response.AnimeIdItemDto
import com.example.network.releases.utils.ReleasesApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for fetching specific anime releases or random selections.
 */
interface ReleasesApi {

    /**
     * Retrieves a list of random anime titles.
     * Useful for discovery features.
     *
     * @param include Fields to include in the response.
     * @param limit Number of random items to return.
     * @return A [Response] containing a list of [AnimeIdItemDto].
     */
    @GET(RANDOM_URL)
    suspend fun getRandomAnime(
        @Query(QUERY_INCLUDE) include: String = ReleasesApiConstants.RANDOM_ANIME_INCLUDE,
        @Query(QUERY_LIMIT) limit: Int = ReleasesApiConstants.RANDOM_ANIME_LIMIT
    ): Response<List<AnimeIdItemDto>>

    /**
     * Fetches detailed information for a specific anime release by its ID.
     *
     * @param id The unique identifier of the release.
     * @param include Specific fields to include for detailed view.
     * @param exclude Fields to omit to optimize payload size.
     * @return A [Response] containing the full [AnimeDetailsItemDto].
     */
    @GET(RELEASE_BY_ID_URL)
    suspend fun getAnime(
        @Path(PARAM_ID) id: Int,
        @Query(QUERY_INCLUDE) include: String = ReleasesApiConstants.CURRENT_ANIME_INCLUDE,
        @Query(QUERY_EXCLUDE) exclude: String = CommonNetworkConstants.COMMON_EXCLUDE
    ): Response<AnimeDetailsItemDto>

    companion object {
        private const val BASE_URL = "anime/releases"

        const val RANDOM_URL = "$BASE_URL/random"

        const val RELEASE_BY_ID_URL = "$BASE_URL/{id}"

        // Query and Path parameter names
        private const val PARAM_ID = "id"
        private const val QUERY_INCLUDE = "include"
        private const val QUERY_EXCLUDE = "exclude"
        private const val QUERY_LIMIT = "limit"
    }
}
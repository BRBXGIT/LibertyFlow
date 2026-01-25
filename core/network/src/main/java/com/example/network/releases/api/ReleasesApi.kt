package com.example.network.releases.api

import com.example.network.common.common_utils.CommonNetworkConstants
import com.example.network.releases.models.anime_details_item_response.AnimeDetailsItemDto
import com.example.network.releases.models.anime_id_item_response.AnimeIdItemDto
import com.example.network.releases.utils.ReleasesApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReleasesApi {

    @GET("anime/releases/random")
    suspend fun getRandomAnime(
        @Query("include") include: String = ReleasesApiConstants.RANDOM_ANIME_INCLUDE,
        @Query("limit") limit: Int = ReleasesApiConstants.RANDOM_ANIME_LIMIT
    ): Response<List<AnimeIdItemDto>>

    @GET("anime/releases/{id}")
    suspend fun getAnime(
        @Path("id") id: Int,
        @Query("include") include: String = ReleasesApiConstants.CURRENT_ANIME_INCLUDE,
        @Query("exclude") exclude: String = CommonNetworkConstants.COMMON_EXCLUDE
    ): Response<AnimeDetailsItemDto>
}
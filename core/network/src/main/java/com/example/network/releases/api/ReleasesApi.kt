package com.example.network.releases.api

import com.example.network.common.common_response_models.AnimeResponseItem
import com.example.network.common.common_utils.CommonNetworkUtils
import com.example.network.releases.models.anime_details_item_response.AnimeDetailsItem
import com.example.network.releases.models.anime_id_item_response.AnimeIdItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReleasesApi {

    @GET("anime/releases/latest")
    suspend fun getLatestAnimeReleases(
        @Query("limit") limit: Int = ReleasesApiUtils.RELEASES_LIMIT,
        @Query("include") include: String = CommonNetworkUtils.COMMON_INCLUDE,
        @Query("exclude") exclude: String = CommonNetworkUtils.COMMON_EXCLUDE
    ): Response<List<AnimeResponseItem>>

    @GET("anime/releases/random")
    suspend fun getRandomAnime(
        @Query("include") include: String = ReleasesApiUtils.RANDOM_ANIME_INCLUDE
    ): Response<AnimeIdItem>

    @GET("anime/releases/{id}")
    suspend fun getAnime(
        @Path("id") id: Int,
        @Query("include") include: String = ReleasesApiUtils.CURRENT_ANIME_INCLUDE,
        @Query("exclude") exclude: String = CommonNetworkUtils.COMMON_EXCLUDE
    ): Response<AnimeDetailsItem>
}
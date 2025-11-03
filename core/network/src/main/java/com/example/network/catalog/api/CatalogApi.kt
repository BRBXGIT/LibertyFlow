package com.example.network.catalog.api

import com.example.network.common.common_utils.CommonUtils
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPagination
import com.example.network.common.common_request_models.common_request.CommonRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CatalogApi {

    @POST("anime/catalog/releases")
    suspend fun getAnimeByFilters(
        @Query("include") include: String = CommonUtils.COMMON_INCLUDE,
        @Query("exclude") exclude: String = CommonUtils.COMMON_EXCLUDE,
        @Body request: CommonRequest
    ): Response<AnimeItemsPagination>
}
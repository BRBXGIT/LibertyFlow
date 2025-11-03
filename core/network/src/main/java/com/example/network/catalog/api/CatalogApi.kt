package com.example.network.catalog.api

import com.example.network.common.common_utils.CommonUtils
import com.example.network.common.pagination.anime_items_pagination.AnimeItemsPagination
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface CatalogApi {

    @POST("anime/catalog/releases")
    suspend fun getAnimeByFilters(
        @Query("include") include: String = CommonUtils.COMMON_INCLUDE,
        @Query("exclude") exclude: String = CommonUtils.COMMON_EXCLUDE,
        @Query("page") page: Int
    ): Response<AnimeItemsPagination>
}
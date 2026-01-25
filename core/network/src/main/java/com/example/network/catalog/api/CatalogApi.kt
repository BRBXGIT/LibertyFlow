package com.example.network.catalog.api

import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CatalogApi {

    @POST("anime/catalog/releases")
    suspend fun getAnimeByFilters(@Body request: CommonRequestDto): Response<AnimeItemsPaginationDto>
}
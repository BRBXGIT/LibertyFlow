package com.example.network.collections.api

import com.example.network.collections.models.ids.CollectionsIdsDto
import com.example.network.collections.models.request.CollectionRequestDto
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDtoWithCollectionTypeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface CollectionsApi {

    @POST("accounts/users/me/collections/releases")
    suspend fun getAnimeInCollection(
        @Header("Authorization") sessionToken: String,
        @Body request: CommonRequestDtoWithCollectionTypeDto
    ): Response<AnimeItemsPaginationDto>

    @GET("accounts/users/me/collections/ids")
    suspend fun getCollectionsIds(
        @Header("Authorization") sessionToken: String,
    ): Response<CollectionsIdsDto>

    @POST("accounts/users/me/collections")
    suspend fun addToCollection(
        @Header("Authorization") sessionToken: String,
        @Body request: CollectionRequestDto
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "accounts/users/me/collections", hasBody = true)
    suspend fun deleteFromCollection(
        @Header("Authorization") sessionToken: String,
        @Body request: CollectionRequestDto
    ): Response<Unit>
}
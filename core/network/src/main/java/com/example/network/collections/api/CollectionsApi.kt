package com.example.network.collections.api

import com.example.network.collections.models.CollectionRequest
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPagination
import com.example.network.common.common_request_models.common_request.CommonRequestWithCollectionType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface CollectionsApi {

    @POST("accounts/users/me/collections/releases")
    suspend fun getAnimeInCollection(
        @Header("Authorization") sessionToken: String,
        @Body request: CommonRequestWithCollectionType
    ): Response<AnimeItemsPagination>

    @POST("accounts/users/me/collections")
    suspend fun addToCollection(
        @Header("Authorization") sessionToken: String,
        @Body request: CollectionRequest
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "accounts/users/me/collections", hasBody = true)
    suspend fun deleteFromCollection(
        @Header("Authorization") sessionToken: String,
        @Body request: CollectionRequest
    ): Response<Unit>
}
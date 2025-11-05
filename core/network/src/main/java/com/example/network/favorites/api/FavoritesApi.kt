package com.example.network.favorites.api

import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPagination
import com.example.network.common.common_request_models.common_request.CommonRequest
import com.example.network.favorites.models.FavoriteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface FavoritesApi {

    @POST("accounts/users/me/favorites/releases")
    suspend fun getFavorites(
        @Header("Authorization") sessionToken: String,
        @Body request: CommonRequest
    ): Response<AnimeItemsPagination>

    @POST("accounts/users/me/favorites")
    suspend fun addFavorite(
        @Header("Authorization") sessionToken: String,
        @Body request: FavoriteRequest
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "accounts/users/me/favorites", hasBody = true)
    suspend fun deleteFavorite(
        @Header("Authorization") sessionToken: String,
        @Body request: FavoriteRequest
    ): Response<Unit>
}
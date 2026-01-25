package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.data.models.favorites.FavoriteRequest
import com.example.data.models.favorites.FavoritesIds
import com.example.data.utils.remote.network_request.NetworkResult
import kotlinx.coroutines.flow.Flow

interface FavoritesRepo {

    fun getFavorites(request: CommonRequest): Flow<PagingData<AnimeItem>>

    suspend fun getFavoritesIds(): NetworkResult<FavoritesIds>

    suspend fun addFavorite(request: FavoriteRequest): NetworkResult<Unit>

    suspend fun deleteFavorite(request: FavoriteRequest): NetworkResult<Unit>
}
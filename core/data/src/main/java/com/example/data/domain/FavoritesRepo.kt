package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.models.favorites.UiFavoriteRequest
import com.example.data.utils.remote.network_request.NetworkResult
import kotlinx.coroutines.flow.Flow

interface FavoritesRepo {

    fun getFavorites(request: UiCommonRequest): Flow<PagingData<UiAnimeItem>>

    suspend fun addFavorite(request: UiFavoriteRequest): NetworkResult<Unit>

    suspend fun deleteFavorite(request: UiFavoriteRequest): NetworkResult<Unit>
}
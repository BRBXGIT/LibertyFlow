package com.example.data.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.domain.FavoritesRepo
import com.example.data.models.common.mappers.toCommonRequest
import com.example.data.models.common.mappers.toUiAnimeItem
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.models.favorites.UiFavoriteRequest
import com.example.data.models.favorites.toFavoriteRequest
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.common.common_utils.CommonNetworkConstants
import com.example.network.favorites.api.FavoritesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoritesRepoImpl @Inject constructor(
    private val favoritesApi: FavoritesApi,
    private val authPrefsManager: AuthPrefsManager
): FavoritesRepo {

    override fun getFavorites(request: UiCommonRequest): Flow<PagingData<UiAnimeItem>> {
        return Pager(
            config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
            pagingSourceFactory = {
                CommonPagingSource(
                    apiCall = { favoritesApi.getFavorites(authPrefsManager.token.firstOrNull()!!, request.toCommonRequest()) },
                    baseRequest = request.toCommonRequest()
                )
            }
        ).flow.map { pagingData -> pagingData.map { it.toUiAnimeItem() } }
    }

    override suspend fun addFavorite(request: UiFavoriteRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = { favoritesApi.addFavorite(token, request.toFavoriteRequest()) },
            map = { it }
        )
    }

    override suspend fun deleteFavorite(request: UiFavoriteRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = { favoritesApi.deleteFavorite(token, request.toFavoriteRequest()) },
            map = { it }
        )
    }
}
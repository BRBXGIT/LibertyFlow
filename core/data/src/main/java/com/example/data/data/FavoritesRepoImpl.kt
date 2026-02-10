package com.example.data.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.domain.FavoritesRepo
import com.example.data.models.common.mappers.toCommonRequestDto
import com.example.data.models.common.mappers.toAnimeItem
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.data.models.favorites.FavoriteRequest
import com.example.data.models.favorites.FavoritesIds
import com.example.data.models.favorites.toFavoriteRequestDto
import com.example.data.models.favorites.toFavoritesIds
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_utils.CommonNetworkConstants
import com.example.network.favorites.api.FavoritesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepoImpl @Inject constructor(
    private val favoritesApi: FavoritesApi,
    private val authPrefsManager: AuthPrefsManager
): FavoritesRepo {

    override fun getFavorites(request: CommonRequest): Flow<PagingData<AnimeItem>> {
        return Pager(
            config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
            pagingSourceFactory = {
                CommonPagingSource(
                    apiCall = { dto ->
                        favoritesApi.getFavorites(
                            sessionToken = authPrefsManager.token.firstOrNull()!!,
                            request = dto as CommonRequestDto
                        )
                    },
                    baseRequest = request.toCommonRequestDto()
                )
            }
        ).flow.map { pagingData -> pagingData.map { it.toAnimeItem() } }
    }

    override suspend fun getFavoritesIds(): NetworkResult<FavoritesIds> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = { favoritesApi.getFavoritesIds(token) },
            map = { it.toFavoritesIds() }
        )
    }

    override suspend fun addFavorite(request: FavoriteRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = { favoritesApi.addFavorite(token, request.toFavoriteRequestDto()) },
            map = {}
        )
    }

    override suspend fun deleteFavorite(request: FavoriteRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = { favoritesApi.deleteFavorite(token, request.toFavoriteRequestDto()) },
            map = {}
        )
    }
}
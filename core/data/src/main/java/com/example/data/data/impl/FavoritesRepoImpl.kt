@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.data.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.data.utils.BaseAuthRepoImpl
import com.example.data.domain.FavoritesRepo
import com.example.data.models.common.mappers.toAnimeItem
import com.example.data.models.common.mappers.toCommonRequestDto
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.anime_item.AnimeItem
import com.example.data.models.favorites.FavoriteItem
import com.example.data.models.favorites.FavoriteRequest
import com.example.data.models.favorites.FavoritesIds
import com.example.data.utils.network.network_request.NetworkRequest
import com.example.data.utils.network.network_request.NetworkResult
import com.example.data.utils.network.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_utils.CommonNetworkConstants
import com.example.network.favorites.api.FavoritesApi
import com.example.network.favorites.models.FavoriteAnimeIdItemDto
import com.example.network.favorites.models.FavoriteIdsResponseDto
import com.example.network.favorites.models.FavoriteRequestDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [FavoritesRepo] for managing user-specific favorite anime.
 * Inherits authenticated session handling from [BaseAuthRepoImpl].
 */
class FavoritesRepoImpl @Inject constructor(
    private val favoritesApi: FavoritesApi,
    authPrefsManager: AuthPrefsManager,
): FavoritesRepo, BaseAuthRepoImpl(authPrefsManager) {

    /**
     * Streams paginated favorite items, automatically refreshing when the [token] updates.
     */
    override fun getFavorites(request: CommonRequest): Flow<PagingData<AnimeItem>> {
        return token.flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = CommonNetworkConstants.COMMON_LIMIT,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    CommonPagingSource(
                        apiCall = { dto ->
                            favoritesApi.getFavorites(
                                sessionToken = token,
                                request = dto as CommonRequestDto
                            )
                        },
                        baseRequest = request.toCommonRequestDto()
                    )
                }
            ).flow
        }.map { pagingData -> pagingData.map { it.toAnimeItem() } }
    }

    /**
     * Retrieves the set of IDs for all anime currently marked as favorites.
     */
    override suspend fun getFavoritesIds(): NetworkResult<FavoritesIds> {
        return NetworkRequest.safeApiCall(
            call = { favoritesApi.getFavoritesIds(token.first()) },
            map = { it.toFavoritesIds() }
        )
    }

    /**
     * Adds an anime or list of anime to the user's favorites list.
     */
    override suspend fun addFavorite(request: FavoriteRequest): NetworkResult<Unit> {
        return NetworkRequest.safeApiCall(
            call = { favoritesApi.addFavorite(token.first(), request.toFavoriteRequestDto()) },
            map = {}
        )
    }

    /**
     * Removes an anime or list of anime from the user's favorites list.
     */
    override suspend fun deleteFavorite(request: FavoriteRequest): NetworkResult<Unit> {
        return NetworkRequest.safeApiCall(
            call = { favoritesApi.deleteFavorite(token.first(), request.toFavoriteRequestDto()) },
            map = {}
        )
    }

    // --- Mappers ---
    private fun FavoriteItem.toFavoriteAnimeIdItemDto() = FavoriteAnimeIdItemDto(id)

    private fun FavoriteRequest.toFavoriteRequestDto(): FavoriteRequestDto {
        val favoriteRequestDto = FavoriteRequestDto()
        favoriteRequestDto.addAll(this.map { it.toFavoriteAnimeIdItemDto() })
        return favoriteRequestDto
    }

    private fun FavoriteIdsResponseDto.toFavoritesIds() =
        FavoritesIds().also { it.addAll(this) }
}
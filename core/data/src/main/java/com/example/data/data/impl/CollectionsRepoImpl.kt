@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.data.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.data.utils.BaseAuthRepoImpl
import com.example.data.domain.CollectionsRepo
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.collections.request.CollectionItem
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.common.mappers.toAnimeItem
import com.example.data.models.common.mappers.toCommonRequestWithCollectionTypeDto
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.anime_item.AnimeItem
import com.example.data.utils.network.network_request.NetworkRequest
import com.example.data.utils.network.network_request.NetworkResult
import com.example.data.utils.network.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.collections.api.CollectionsApi
import com.example.network.collections.models.ids.CollectionsIdsDto
import com.example.network.collections.models.request.CollectionItemDto
import com.example.network.collections.models.request.CollectionRequestDto
import com.example.network.common.common_request_models.common_request.CommonRequestDtoWithCollectionTypeDto
import com.example.network.common.common_utils.CommonNetworkConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [CollectionsRepo] handling user-specific anime collections.
 * Extends [BaseAuthRepoImpl] to manage session token for authenticated API calls.
 */
class CollectionsRepoImpl @Inject constructor(
    private val collectionsApi: CollectionsApi,
    authPrefsManager: AuthPrefsManager
): CollectionsRepo, BaseAuthRepoImpl(authPrefsManager) {

    /**
     * Provides a reactive stream of paginated anime from a specific collection.
     * Automatically reacts to [token] changes using [flatMapLatest].
     */
    override fun getAnimeInCollection(request: CommonRequestWithCollectionType): Flow<PagingData<AnimeItem>> {
        return token.flatMapLatest { token ->
            Pager(
                config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
                pagingSourceFactory = {
                    CommonPagingSource(
                        apiCall = { dto ->
                            collectionsApi.getAnimeInCollection(
                                sessionToken = token,
                                request = dto as CommonRequestDtoWithCollectionTypeDto
                            )
                        },
                        baseRequest = request.toCommonRequestWithCollectionTypeDto()
                    )
                }
            ).flow
        }
            .map { pagingData -> pagingData.map { it.toAnimeItem() } }
    }

    /**
     * Fetches the list of all collection IDs associated with the current user.
     */
    override suspend fun getCollectionsIds(): NetworkResult<List<AnimeCollection>> {
        return NetworkRequest.safeApiCall(
            call = { collectionsApi.getCollectionsIds(token.first()) },
            map = { it.toAnimeCollection() }
        )
    }

    /**
     * Adds an anime or list of anime to a specific collection.
     */
    override suspend fun addToCollection(request: CollectionRequest): NetworkResult<Unit> {
        return NetworkRequest.safeApiCall(
            call = {
                collectionsApi.addToCollection(
                    sessionToken = token.first(),
                    request = request.toCollectionRequestDto()
                )
            },
            map = {}
        )
    }

    /**
     * Removes an anime or list of anime from a specific collection.
     */
    override suspend fun deleteFromCollection(request: CollectionRequest): NetworkResult<Unit> {
        return NetworkRequest.safeApiCall(
            call = {
                collectionsApi.deleteFromCollection(
                    sessionToken = token.first(),
                    request = request.toCollectionRequestDto()
                )
            },
            map = {}
        )
    }

    // --- Mappers ---
    private fun CollectionItem.toCollectionItemDto(): CollectionItemDto {
        return CollectionItemDto(
            releaseId = id,
            collectionType = collection.name
        )
    }

    private fun CollectionRequest.toCollectionRequestDto(): CollectionRequestDto {
        val collectionRequestDto = CollectionRequestDto()
        collectionRequestDto.addAll(this.map { it.toCollectionItemDto() })
        return collectionRequestDto
    }

    private fun CollectionsIdsDto.toAnimeCollection(): List<AnimeCollection> {
        return this.mapNotNull { item ->
            if (item.size < 2) return@mapNotNull null

            val id = when (val idValue = item[0]) {
                is Number -> idValue.toInt()
                is String -> idValue.toIntOrNull()
                else -> null
            }

            val status = try {
                Collection.valueOf(item[1].toString())
            } catch (e: IllegalArgumentException) {
                null
            }

            if (id != null && status != null) {
                AnimeCollection(status, id)
            } else {
                null
            }
        }
    }
}
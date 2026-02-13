@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.data.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.data.utils.BaseAuthRepoImpl
import com.example.data.domain.CollectionsRepo
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.collections.mappers.toAnimeCollection
import com.example.data.models.collections.mappers.toCollectionRequestDto
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.common.mappers.toAnimeItem
import com.example.data.models.common.mappers.toCommonRequestWithCollectionTypeDto
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.data.utils.network.network_request.NetworkRequest
import com.example.data.utils.network.network_request.NetworkResult
import com.example.data.utils.network.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.collections.api.CollectionsApi
import com.example.network.common.common_request_models.common_request.CommonRequestDtoWithCollectionTypeDto
import com.example.network.common.common_utils.CommonNetworkConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionsRepoImpl @Inject constructor(
    private val collectionsApi: CollectionsApi,
    private val authPrefsManager: AuthPrefsManager
): CollectionsRepo, BaseAuthRepoImpl(authPrefsManager) {

    override fun getAnimeInCollection(request: CommonRequestWithCollectionType): Flow<PagingData<AnimeItem>> {
        return token.flatMapLatest {
                Pager(
                    config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
                    pagingSourceFactory = {
                        CommonPagingSource(
                            apiCall = { dto ->
                                collectionsApi.getAnimeInCollection(
                                    sessionToken = authPrefsManager.token.firstOrNull()!!,
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

    override suspend fun getCollectionsIds(): NetworkResult<List<AnimeCollection>> {
        return NetworkRequest.safeApiCall(
            call = { collectionsApi.getCollectionsIds(token.first()) },
            map = { it.toAnimeCollection() }
        )
    }

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
}
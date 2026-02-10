package com.example.data.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.domain.CollectionsRepo
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.collections.mappers.toAnimeCollections
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.collections.mappers.toCollectionRequestDto
import com.example.data.models.common.mappers.toCommonRequestWithCollectionTypeDto
import com.example.data.models.common.mappers.toAnimeItem
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.collections.api.CollectionsApi
import com.example.network.common.common_utils.CommonNetworkConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionsRepoImpl @Inject constructor(
    private val collectionsApi: CollectionsApi,
    private val authPrefsManager: AuthPrefsManager
): CollectionsRepo {

    override fun getAnimeInCollection(request: CommonRequestWithCollectionType): Flow<PagingData<AnimeItem>> {
        return Pager(
            config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
            pagingSourceFactory = {
                CommonPagingSource(
                    apiCall = {
                        collectionsApi.getAnimeInCollection(
                            sessionToken = authPrefsManager.token.firstOrNull()!!,
                            request = request.toCommonRequestWithCollectionTypeDto()
                        )
                    },
                    baseRequest = request.toCommonRequestWithCollectionTypeDto()
                )
            }
        ).flow.map { pagingData -> pagingData.map { it.toAnimeItem() } }
    }

    override suspend fun getCollectionsIds(): NetworkResult<List<AnimeCollection>> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = { collectionsApi.getCollectionsIds(token) },
            map = { it.toAnimeCollections() }
        )
    }

    override suspend fun addToCollection(request: CollectionRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = {
                collectionsApi.addToCollection(
                    sessionToken = token,
                    request = request.toCollectionRequestDto()
                )
            },
            map = {}
        )
    }

    override suspend fun deleteFromCollection(request: CollectionRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = {
                collectionsApi.deleteFromCollection(
                    sessionToken = token,
                    request = request.toCollectionRequestDto()
                )
            },
            map = {}
        )
    }
}
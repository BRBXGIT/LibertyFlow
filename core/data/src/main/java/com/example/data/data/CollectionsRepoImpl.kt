package com.example.data.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.domain.CollectionsRepo
import com.example.data.models.collections.UiCollectionRequest
import com.example.data.models.collections.toCollectionRequest
import com.example.data.models.common.mappers.toCommonRequestWithCollectionType
import com.example.data.models.common.mappers.toUiAnimeItem
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.local.auth.AuthPrefsManager
import com.example.network.collections.api.CollectionsApi
import com.example.network.common.common_utils.CommonNetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CollectionsRepoImpl @Inject constructor(
    private val collectionsApi: CollectionsApi,
    private val authPrefsManager: AuthPrefsManager
): CollectionsRepo {

    override suspend fun getAnimeInCollection(request: UiCommonRequestWithCollectionType): Flow<PagingData<UiAnimeItem>> {
        val token = authPrefsManager.token.firstOrNull()!!

        return Pager(
            config = PagingConfig(pageSize = CommonNetworkUtils.COMMON_LIMIT, enablePlaceholders = false),
            pagingSourceFactory = {
                CommonPagingSource(
                    apiCall = {
                        collectionsApi.getAnimeInCollection(
                            sessionToken = token,
                            request = request.toCommonRequestWithCollectionType()
                        )
                    },
                    baseRequest = request.toCommonRequestWithCollectionType()
                )
            }
        ).flow.map { pagingData -> pagingData.map { it.toUiAnimeItem() } }
    }

    override suspend fun addToCollection(request: UiCollectionRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = {
                collectionsApi.addToCollection(
                    sessionToken = token,
                    request = request.toCollectionRequest()
                )
            },
            map = { it }
        )
    }

    override suspend fun deleteFromCollection(request: UiCollectionRequest): NetworkResult<Unit> {
        val token = authPrefsManager.token.first()!!

        return NetworkRequest.safeApiCall(
            call = {
                collectionsApi.deleteFromCollection(
                    sessionToken = token,
                    request = request.toCollectionRequest()
                )
            },
            map = { it }
        )
    }
}
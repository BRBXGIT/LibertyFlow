package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.collections.collection_ids.UiCollectionIds
import com.example.data.models.collections.request.UiCollectionRequest
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.utils.remote.network_request.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CollectionsRepo {

    fun getAnimeInCollection(
        request: UiCommonRequestWithCollectionType
    ): Flow<PagingData<UiAnimeItem>>

    suspend fun getCollectionsIds(): NetworkResult<UiCollectionIds>

    suspend fun addToCollection(request: UiCollectionRequest): NetworkResult<Unit>

    suspend fun deleteFromCollection(request: UiCollectionRequest): NetworkResult<Unit>
}
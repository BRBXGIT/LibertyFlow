package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.collections.UiCollectionRequest
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.utils.remote.network_request.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CollectionsRepo {

    fun getAnimeInCollection(
        request: UiCommonRequestWithCollectionType
    ): Flow<PagingData<UiAnimeItem>>

    suspend fun addToCollection(request: UiCollectionRequest): NetworkResult<Unit>

    suspend fun deleteFromCollection(request: UiCollectionRequest): NetworkResult<Unit>
}
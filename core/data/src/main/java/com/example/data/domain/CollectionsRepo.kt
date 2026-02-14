package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.anime_item.AnimeItem
import com.example.data.utils.network.network_request.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CollectionsRepo {

    fun getAnimeInCollection(
        request: CommonRequestWithCollectionType
    ): Flow<PagingData<AnimeItem>>

    suspend fun getCollectionsIds(): NetworkResult<List<AnimeCollection>>

    suspend fun addToCollection(request: CollectionRequest): NetworkResult<Unit>

    suspend fun deleteFromCollection(request: CollectionRequest): NetworkResult<Unit>
}
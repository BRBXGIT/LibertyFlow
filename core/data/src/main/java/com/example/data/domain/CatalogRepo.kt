package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.ui_anime_item.AnimeItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepo {

    fun getAnimeByQuery(request: CommonRequest): Flow<PagingData<AnimeItem>>
}
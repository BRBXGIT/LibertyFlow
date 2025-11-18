package com.example.data.domain

import androidx.paging.PagingData
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepo {

    fun getAnimeByQuery(request: UiCommonRequest): Flow<PagingData<UiAnimeItem>>
}
package com.example.data.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.domain.CatalogRepo
import com.example.data.models.common.mappers.toCommonRequest
import com.example.data.models.common.mappers.toUiAnimeItem
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.network.catalog.api.CatalogApi
import com.example.network.common.common_utils.CommonNetworkConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatalogRepoImpl @Inject constructor(
    private val catalogApi: CatalogApi
): CatalogRepo {

    override fun getAnimeByQuery(request: UiCommonRequest): Flow<PagingData<UiAnimeItem>> {
        return Pager(
            config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
            pagingSourceFactory = {
                CommonPagingSource(
                    apiCall = { catalogApi.getAnimeByFilters(request.toCommonRequest()) },
                    baseRequest = request.toCommonRequest()
                )
            }
        ).flow.map { pagingData -> pagingData.map { it.toUiAnimeItem() } }
    }
}
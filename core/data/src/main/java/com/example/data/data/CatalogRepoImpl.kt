package com.example.data.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.domain.CatalogRepo
import com.example.data.models.common.mappers.toCommonRequestDto
import com.example.data.models.common.mappers.toAnimeItem
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.network.catalog.api.CatalogApi
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_utils.CommonNetworkConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatalogRepoImpl @Inject constructor(
    private val catalogApi: CatalogApi
): CatalogRepo {

    override fun getAnimeByQuery(request: CommonRequest): Flow<PagingData<AnimeItem>> {
        return Pager(
            config = PagingConfig(pageSize = CommonNetworkConstants.COMMON_LIMIT, enablePlaceholders = false),
            pagingSourceFactory = {
                CommonPagingSource(
                    apiCall = { dto -> catalogApi.getAnimeByFilters(dto as CommonRequestDto) },
                    baseRequest = request.toCommonRequestDto()
                )
            }
        ).flow.map { pagingData -> pagingData.map { it.toAnimeItem() } }
    }
}
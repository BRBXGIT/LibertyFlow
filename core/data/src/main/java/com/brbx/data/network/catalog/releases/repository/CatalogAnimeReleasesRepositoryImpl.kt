package com.brbx.data.network.catalog.releases.repository

import androidx.paging.PagingData
import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.common.toData
import com.brbx.data.network.paging.map.toDomain
import com.brbx.data.network.paging.source.createAnimeItemsPagingFlow
import com.brbx.domain.network.catalog.releases.repository.CatalogAnimeReleasesRepository
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.network.anime_catalog.releases.api.AnimeCatalogReleasesApi
import kotlinx.coroutines.flow.Flow

internal class CatalogAnimeReleasesRepositoryImpl(
    private val api: AnimeCatalogReleasesApi,
    private val executor: ApiCallExecutor,
) : CatalogAnimeReleasesRepository {

    override fun getReleases(request: DomainRequest.Complex): Flow<PagingData<DomainAnimeItem>> {
        val dataRequest = request.toData()
        return createAnimeItemsPagingFlow(
            limit = dataRequest.limit,
            call = { page ->
                val withPage = dataRequest.copy(page = page)
                executor.execute(mapper = { it.toDomain() }) { api.getReleases(request = withPage) }
            }
        )
    }
}
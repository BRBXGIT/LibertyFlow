package com.brbx.data.catalog.releases.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.brbx.data.common.map.toData
import com.brbx.data.common.map.toDomain
import com.brbx.data.paging.anime_item.createAnimeItemsPagingFlow
import com.brbx.domain.catalog.releases.repository.CatalogAnimeReleasesRepository
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.network.anime_catalog.releases.api.AnimeCatalogReleasesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CatalogAnimeReleasesRepositoryImpl(
    private val api: AnimeCatalogReleasesApi,
) : CatalogAnimeReleasesRepository {

    override fun getReleases(request: DomainRequest.Complex): Flow<PagingData<DomainAnimeItem>> {
        val dataRequest = request.toData()
        return createAnimeItemsPagingFlow(
            limit = dataRequest.limit,
            call = { page ->
                val withPage = dataRequest.copy(page = page)
                api.getReleases(request = withPage)
            }
        ).map { pagingData -> pagingData.map { it.toDomain() } }
    }
}
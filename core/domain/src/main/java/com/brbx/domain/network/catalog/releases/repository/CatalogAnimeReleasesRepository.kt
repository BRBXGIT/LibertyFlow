package com.brbx.domain.network.catalog.releases.repository

import androidx.paging.PagingData
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface CatalogAnimeReleasesRepository {

    fun getReleases(request: DomainRequest.Complex): Flow<PagingData<DomainAnimeItem>>
}
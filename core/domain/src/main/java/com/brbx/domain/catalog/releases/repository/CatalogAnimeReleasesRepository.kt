package com.brbx.domain.catalog.releases.repository

import androidx.paging.PagingData
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface CatalogAnimeReleasesRepository {

    fun getReleases(request: DomainRequest.Complex): Flow<PagingData<DomainAnimeItem>>
}
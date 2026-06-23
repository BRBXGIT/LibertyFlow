package com.brbx.domain.network.catalog.releases.use_case

import androidx.paging.PagingData
import com.brbx.domain.network.catalog.releases.model.CatalogReleasesParameters
import com.brbx.domain.network.catalog.releases.repository.CatalogAnimeReleasesRepository
import com.brbx.domain.network.model.common.PublishStatus
import com.brbx.domain.network.model.request.DomainParameters
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

class GetCatalogAnimeReleasesUseCase(
    private val repository: CatalogAnimeReleasesRepository,
) {
    operator fun invoke(
        parameters: CatalogReleasesParameters
    ): Flow<PagingData<DomainAnimeItem>> {
        val publishStatuses = if (parameters.isOngoing) {
            listOf(PublishStatus.Ongoing)
        } else listOf(PublishStatus.Finished)
        val request = DomainRequest.Complex(
            parameters = DomainParameters.Complex(
                publishStatuses = publishStatuses,
                sorting = parameters.sorting,
                years = parameters.years,
                seasons = parameters.seasons,
                genres = parameters.genres,
            )
        )

        return repository.getReleases(request)
    }
}
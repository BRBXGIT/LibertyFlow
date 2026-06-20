package com.brbx.domain.catalog.releases.use_case

import androidx.paging.PagingData
import com.brbx.domain.catalog.releases.model.CatalogReleasesParameters
import com.brbx.domain.catalog.releases.repository.CatalogAnimeReleasesRepository
import com.brbx.domain.model.common.PublishStatus
import com.brbx.domain.model.request.DomainParameters
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

class GetCatalogAnimeReleasesUseCase(
    private val repository: CatalogAnimeReleasesRepository,
) {
    suspend operator fun invoke(
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
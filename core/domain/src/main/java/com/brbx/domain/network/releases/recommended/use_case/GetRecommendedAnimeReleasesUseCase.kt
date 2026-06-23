package com.brbx.domain.network.releases.recommended.use_case

import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.recommended.repository.RecommendedAnimeReleasesRepository

class GetRecommendedAnimeReleasesUseCase(
    private val repository: RecommendedAnimeReleasesRepository,
) {
    suspend operator fun invoke(id: Int): DomainRequestResult<List<DomainAnimeItem>> =
        repository.getReleases(id)
}
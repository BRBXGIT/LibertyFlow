package com.brbx.domain.releases.recommended.use_case

import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.recommended.repository.RecommendedAnimeReleasesRepository

class GetRecommendedAnimeReleasesUseCase(
    private val repository: RecommendedAnimeReleasesRepository,
) {
    suspend operator fun invoke(id: Int): DomainRequestResult<List<DomainAnimeItem>> =
        repository.getReleases(id)
}
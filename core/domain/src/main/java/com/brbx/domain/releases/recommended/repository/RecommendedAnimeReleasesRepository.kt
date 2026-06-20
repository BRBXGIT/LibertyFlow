package com.brbx.domain.releases.recommended.repository

import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.model.result.DomainRequestResult

interface RecommendedAnimeReleasesRepository {

    suspend fun getReleases(id: Int): DomainRequestResult<List<DomainAnimeItem>>
}
package com.brbx.domain.network.releases.recommended.repository

import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.result.DomainRequestResult

interface RecommendedAnimeReleasesRepository {

    suspend fun getReleases(releaseId: Int): DomainRequestResult<List<DomainAnimeItem>>
}
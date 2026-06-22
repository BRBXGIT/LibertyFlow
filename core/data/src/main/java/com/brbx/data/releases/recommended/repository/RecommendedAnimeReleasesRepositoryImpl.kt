package com.brbx.data.releases.recommended.repository

import com.brbx.data.common.map.toDomain
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.recommended.repository.RecommendedAnimeReleasesRepository
import com.brbx.network.anime_releases.recommened.api.AnimeReleasesRecommendedApi

internal class RecommendedAnimeReleasesRepositoryImpl(
    private val api: AnimeReleasesRecommendedApi
) : RecommendedAnimeReleasesRepository {

    override suspend fun getReleases(releaseId: Int): DomainRequestResult<List<DomainAnimeItem>> =
        api.getReleases(releaseId).toDomain { list -> list.map { item -> item.toDomain() } }
}
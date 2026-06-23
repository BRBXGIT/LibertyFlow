package com.brbx.data.network.releases.recommended.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.common.toDomain
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.recommended.repository.RecommendedAnimeReleasesRepository
import com.brbx.network.anime_releases.recommened.api.AnimeReleasesRecommendedApi

internal class RecommendedAnimeReleasesRepositoryImpl(
    private val api: AnimeReleasesRecommendedApi,
    private val executor: ApiCallExecutor,
) : RecommendedAnimeReleasesRepository {

    override suspend fun getReleases(releaseId: Int): DomainRequestResult<List<DomainAnimeItem>> =
        executor.execute(
            mapper = { list -> list.map { it.toDomain() } }
        ) { api.getReleases(releaseId) }
}
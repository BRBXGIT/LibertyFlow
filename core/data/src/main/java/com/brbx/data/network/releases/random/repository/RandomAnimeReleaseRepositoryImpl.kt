package com.brbx.data.network.releases.random.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.random.model.DomainRandomAnime
import com.brbx.domain.network.releases.random.repository.RandomAnimeReleaseRepository
import com.brbx.network.anime_releases.random.api.AnimeReleasesRandomApi
import com.brbx.network.anime_releases.random.model.RandomAnime

internal class RandomAnimeReleaseRepositoryImpl(
    private val api: AnimeReleasesRandomApi,
    private val executor: ApiCallExecutor,
) : RandomAnimeReleaseRepository {

    override suspend fun getRelease(): DomainRequestResult<DomainRandomAnime> =
        executor.execute(
            mapper = { list -> list.first().toDomain() }
        ) { api.getReleases() }

    private fun RandomAnime.toDomain(): DomainRandomAnime =
        DomainRandomAnime(id = this.id)
}
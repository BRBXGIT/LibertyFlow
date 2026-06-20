package com.brbx.data.releases.random

import com.brbx.data.common.map.toDomain
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.random.model.DomainRandomAnime
import com.brbx.domain.releases.random.repository.RandomAnimeReleaseRepository
import com.brbx.network.anime_releases.random.api.AnimeReleasesRandomApi
import com.brbx.network.anime_releases.random.model.RandomAnime

internal class RandomAnimeReleaseRepositoryImpl(
    private val api: AnimeReleasesRandomApi
) : RandomAnimeReleaseRepository {

    override suspend fun getRelease(): DomainRequestResult<DomainRandomAnime> =
        api.getReleases().toDomain { list -> list.first().toDomain() }

    private fun RandomAnime.toDomain(): DomainRandomAnime =
        DomainRandomAnime(id = this.id)
}
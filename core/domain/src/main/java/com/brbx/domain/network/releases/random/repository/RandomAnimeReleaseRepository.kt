package com.brbx.domain.network.releases.random.repository

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.random.model.DomainRandomAnime

interface RandomAnimeReleaseRepository {

    suspend fun getRelease(): DomainRequestResult<DomainRandomAnime>
}
package com.brbx.domain.releases.random.repository

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.random.model.DomainRandomAnime

interface RandomAnimeReleaseRepository {

    suspend fun getRelease(): DomainRequestResult<DomainRandomAnime>
}
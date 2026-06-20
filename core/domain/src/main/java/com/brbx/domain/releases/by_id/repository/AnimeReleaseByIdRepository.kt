package com.brbx.domain.releases.by_id.repository

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.by_id.model.DomainAnimeItemById

interface AnimeReleaseByIdRepository {

    suspend fun getRelease(id: Int): DomainRequestResult<DomainAnimeItemById>
}
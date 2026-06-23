package com.brbx.domain.network.releases.by_id.repository

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.by_id.model.DomainAnimeItemById

interface AnimeReleaseByIdRepository {

    suspend fun getRelease(id: Int): DomainRequestResult<DomainAnimeItemById>
}
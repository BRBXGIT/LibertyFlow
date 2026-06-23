package com.brbx.domain.network.releases.by_id.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.by_id.model.DomainAnimeItemById
import com.brbx.domain.network.releases.by_id.repository.AnimeReleaseByIdRepository

class GetAnimeReleaseByIdUseCase(
    private val repository: AnimeReleaseByIdRepository,
) {
    suspend operator fun invoke(id: Int): DomainRequestResult<DomainAnimeItemById> =
        repository.getRelease(id)
}
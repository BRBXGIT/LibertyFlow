package com.brbx.domain.releases.by_id.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.by_id.model.DomainAnimeItemById
import com.brbx.domain.releases.by_id.repository.AnimeReleaseByIdRepository

class GetAnimeReleaseByIdUseCase(
    private val repository: AnimeReleaseByIdRepository,
) {
    suspend operator fun invoke(id: Int): DomainRequestResult<DomainAnimeItemById> =
        repository.getRelease(id)
}
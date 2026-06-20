package com.brbx.domain.releases.random.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.releases.random.model.DomainRandomAnime
import com.brbx.domain.releases.random.repository.RandomAnimeReleaseRepository

class GetRandomAnimeReleaseUseCase(
    private val repository: RandomAnimeReleaseRepository,
) {
    suspend operator fun invoke(): DomainRequestResult<DomainRandomAnime> =
        repository.getRelease()
}
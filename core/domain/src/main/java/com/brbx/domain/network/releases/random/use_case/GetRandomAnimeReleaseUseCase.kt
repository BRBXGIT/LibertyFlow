package com.brbx.domain.network.releases.random.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.releases.random.model.DomainRandomAnime
import com.brbx.domain.network.releases.random.repository.RandomAnimeReleaseRepository

class GetRandomAnimeReleaseUseCase(
    private val repository: RandomAnimeReleaseRepository,
) {
    suspend operator fun invoke(): DomainRequestResult<DomainRandomAnime> =
        repository.getRelease()
}
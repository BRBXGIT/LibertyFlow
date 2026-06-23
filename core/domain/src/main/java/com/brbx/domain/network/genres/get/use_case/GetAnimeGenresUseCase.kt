package com.brbx.domain.network.genres.get.use_case

import com.brbx.domain.network.genres.get.repository.AnimeGenresRepository
import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.result.DomainRequestResult

class GetAnimeGenresUseCase(
    private val repository: AnimeGenresRepository,
) {
    suspend operator fun invoke(): DomainRequestResult<List<DomainGenre>> =
        repository.getGenres()
}
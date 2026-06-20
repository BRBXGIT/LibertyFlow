package com.brbx.domain.genres.get_genres.use_case

import com.brbx.domain.genres.get_genres.repository.AnimeGenresRepository
import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.result.DomainRequestResult

class GetAnimeGenresUseCase(
    private val repository: AnimeGenresRepository,
) {
    suspend operator fun invoke(): DomainRequestResult<List<DomainGenre>> =
        repository.getGenres()
}
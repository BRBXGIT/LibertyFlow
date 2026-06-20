package com.brbx.domain.genres.get_genres.repository

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.result.DomainRequestResult

interface AnimeGenresRepository {

    suspend fun getGenres(): DomainRequestResult<List<DomainGenre>>
}
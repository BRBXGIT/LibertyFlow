package com.brbx.domain.network.genres.get.repository

import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.result.DomainRequestResult

interface AnimeGenresRepository {

    suspend fun getGenres(): DomainRequestResult<List<DomainGenre>>
}
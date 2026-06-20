package com.brbx.data.genres.get

import com.brbx.data.common.map.toDomain
import com.brbx.domain.genres.get.repository.AnimeGenresRepository
import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.network.anime_genres.genres.api.AnimeGenresApi

internal class AnimeGenresRepositoryImpl(
    private val api: AnimeGenresApi,
) : AnimeGenresRepository {

    override suspend fun getGenres(): DomainRequestResult<List<DomainGenre>> =
        api.getGenres().toDomain { list -> list.map { genre -> genre.toDomain() } }
}
package com.brbx.data.network.genres.get.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.common.toDomain
import com.brbx.domain.network.genres.get.repository.AnimeGenresRepository
import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.network.anime_genres.genres.api.AnimeGenresApi

internal class AnimeGenresRepositoryImpl(
    private val api: AnimeGenresApi,
    private val executor: ApiCallExecutor,
) : AnimeGenresRepository {

    override suspend fun getGenres(): DomainRequestResult<List<DomainGenre>> =
        executor.execute(
            mapper = { list -> list.map { it.toDomain() } }
        ) {
            api.getGenres()
        }
}
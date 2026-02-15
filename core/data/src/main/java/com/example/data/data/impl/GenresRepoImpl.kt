package com.example.data.data.impl

import com.example.data.domain.GenresRepo
import com.example.data.models.common.common.Genre
import com.example.data.models.common.mappers.toGenre
import com.example.data.utils.network.network_caller.NetworkCaller
import com.example.data.utils.network.network_caller.NetworkResult
import com.example.network.genres.api.GenresApi
import javax.inject.Inject

/**
 * Implementation of [GenresRepo] providing static data for anime genres.
 */
class GenresRepoImpl @Inject constructor(
    private val networkCaller: NetworkCaller,
    private val genresApi: GenresApi
): GenresRepo {

    /**
     * Fetches all available genres from the remote API.
     * Maps the resulting DTO list to domain [Genre] objects.
     */
    override suspend fun getGenres(): NetworkResult<List<Genre>> {
        return networkCaller.safeApiCall(
            call = { genresApi.getGenres() },
            map = { list -> list.map { it.toGenre() } }
        )
    }
}
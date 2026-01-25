package com.example.data.data

import com.example.data.domain.GenresRepo
import com.example.data.models.common.common.Genre
import com.example.data.models.common.mappers.toGenre
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.network.genres.api.GenresApi
import javax.inject.Inject

class GenresRepoImpl @Inject constructor(
    private val genresApi: GenresApi
): GenresRepo {

    override suspend fun getGenres(): NetworkResult<List<Genre>> {
        return NetworkRequest.safeApiCall(
            call = { genresApi.getGenres() },
            map = { list -> list.map { it.toGenre() } }
        )
    }
}
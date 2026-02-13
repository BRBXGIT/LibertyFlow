package com.example.data.domain

import com.example.data.models.common.common.Genre
import com.example.data.utils.network.network_request.NetworkResult

interface GenresRepo {

    suspend fun getGenres(): NetworkResult<List<Genre>>
}
package com.example.data.domain

import com.example.data.models.common.common.UiGenre
import com.example.data.utils.remote.network_request.NetworkResult

interface GenresRepo {

    suspend fun getGenres(): NetworkResult<List<UiGenre>>
}
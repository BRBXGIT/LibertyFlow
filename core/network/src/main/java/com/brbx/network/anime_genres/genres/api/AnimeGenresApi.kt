package com.brbx.network.anime_genres.genres.api

import com.brbx.network.base.model.common.Genre
import com.brbx.network.base.model.result.RequestResult

interface AnimeGenresApi {

    suspend fun getGenres(): RequestResult<List<Genre>>
}
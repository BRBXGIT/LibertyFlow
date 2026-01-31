package com.example.network.genres.api

import com.example.network.common.common_response_models.GenreDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresApi {

    @GET("anime/genres")
    suspend fun getGenres(
        @Query("include") include: String = GenresUtils.GENRES_INCLUDE
    ): Response<List<GenreDto>>
}
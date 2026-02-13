package com.example.network.genres.api

import com.example.network.common.common_response_models.GenreDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for retrieving anime genres metadata.
 */
interface GenresApi {

    /**
     * Fetches a list of all available genres from the catalog.
     *
     * @param include A comma-separated string of fields to include in the response.
     * Defaults to [GenresUtils.GENRES_INCLUDE].
     * @return A [Response] containing a list of [GenreDto] objects.
     */
    @GET(GENRES_URL)
    suspend fun getGenres(
        @Query(QUERY_INCLUDE) include: String = GenresUtils.GENRES_INCLUDE
    ): Response<List<GenreDto>>

    companion object {
        const val GENRES_URL = "anime/genres"

        private const val QUERY_INCLUDE = "include"
    }
}
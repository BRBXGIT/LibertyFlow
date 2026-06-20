package com.brbx.network.anime_genres.genres.api

import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.common.RequestParameters
import com.brbx.network.base.model.common.Genre
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class AnimeGenresApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AnimeGenresApi {

    override suspend fun getGenres(): RequestResult<List<Genre>> =
        apiCallExecutor.execute {
            apiClientProvider.client.get(urlString = AnimeGenresApiDefaults.GenresEndPoint) {
                parameter(RequestParameters.Include, Include)
            }.body()
        }


    private companion object {

        const val Include = "id,name"
    }
}

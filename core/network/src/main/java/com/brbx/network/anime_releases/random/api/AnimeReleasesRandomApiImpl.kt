package com.brbx.network.anime_releases.random.api

import com.brbx.network.anime_releases.random.model.RandomAnime
import com.brbx.network.base.executor.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.common.RequestDefaults
import com.brbx.network.base.common.RequestParameters
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class AnimeReleasesRandomApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AnimeReleasesRandomApi {

    override suspend fun getReleases(limit: Int): RequestResult<List<RandomAnime>> =
        apiCallExecutor.execute {
            apiClientProvider.client.get(urlString = AnimeReleasesRandomDefaults.RandomEndPoint) {
                parameter(RequestParameters.Include, Include)
                parameter(RequestParameters.Exclude, RequestDefaults.Exclude)
                parameter(RequestParameters.Limit, limit)
            }.body()
        }

    private companion object {

        const val Include = "id"
    }
}
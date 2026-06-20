package com.brbx.network.anime_releases.recommened.api

import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.common.RequestDefaults
import com.brbx.network.base.common.RequestParameters
import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class AnimeReleasesRecommendedApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AnimeReleasesRecommendedApi {

    override suspend fun getReleases(
        releaseId: Int,
        limit: Int,
    ): RequestResult<List<AnimeItem>> =
        apiCallExecutor.execute {
            apiClientProvider.client.get(
                urlString = AnimeReleasesRecommendedDefaults.RecommendationsEndPoint,
            ) {
                parameter(RequestParameters.Include, RequestDefaults.Include)
                parameter(RequestParameters.Exclude, RequestDefaults.Exclude)
                parameter(RequestParameters.Limit, limit)
                parameter(RequestParameters.ReleaseId, releaseId)
            }.body()
        }
}
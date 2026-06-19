package com.brbx.network.anime_catalog.releases.api

import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.request.Request
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AnimeCatalogReleasesApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AnimeCatalogReleasesApi {

    override suspend fun getReleases(
        request: Request.Complex,
    ): RequestResult<PaginatedAnimeItems> =
        apiCallExecutor.execute {
            apiClientProvider.client.post(urlString = ReleasesEndPoint) {
                setBody(request)
            }.body()
        }

    private companion object {

        const val ReleasesEndPoint = "anime/catalog/releases"
    }
}
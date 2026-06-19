package com.brbx.network.account_user.lists.collections_releases.api

import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.request.Request
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AccountUserCollectionsReleasesApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserCollectionsReleasesApi {

    override suspend fun getCollection(request: Request.Collection):
        RequestResult<PaginatedAnimeItems> =
            apiCallExecutor.execute {
                apiClientProvider.client.post(urlString = CollectionsReleasesEndPoint) {
                    setBody(request)
                }.body()
            }

    private companion object {

        const val CollectionsReleasesEndPoint = "accounts/users/me/collections/releases"
    }
}
package com.brbx.network.account_user.lists.favorites.favorites_releases.api

import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.request.Request
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AccountUserFavoritesReleasesApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserFavoritesReleasesApi {

    override suspend fun getFavorites(request: Request.Simple): RequestResult<PaginatedAnimeItems> =
        apiCallExecutor.execute {
            apiClientProvider.client.post(urlString = FavoritesReleasesEndPoint) {
                setBody(request)
            }.body()
        }

    private companion object {

        const val FavoritesReleasesEndPoint = "accounts/users/me/favorites/releases"
    }
}
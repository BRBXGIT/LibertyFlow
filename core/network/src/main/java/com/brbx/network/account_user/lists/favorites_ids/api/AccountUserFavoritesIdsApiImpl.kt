package com.brbx.network.account_user.lists.favorites_ids.api

import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class AccountUserFavoritesIdsApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserFavoritesIdsApi {

    override suspend fun getIds(): RequestResult<List<Int>> =
        apiCallExecutor.execute {
            apiClientProvider.client.get(urlString = FavoritesIdsEndPoint).body()
        }

    private companion object {
        const val FavoritesIdsEndPoint = "accounts/users/me/favorites/ids"
    }
}
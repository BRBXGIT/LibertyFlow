package com.brbx.network.account_user.lists.favorites.api

import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AccountUserFavoritesApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserFavoritesApi {

    override suspend fun addToFavorites(items: List<UserListItem.Favorite>): RequestResult<Unit> =
        apiCallExecutor.execute {
            apiClientProvider.client.post(
                urlString = FavoritesEndPoint,
            ) { setBody(items) }
        }

    override suspend fun deleteFromFavorites(items: List<UserListItem.Favorite>):
        RequestResult<Unit> =
            apiCallExecutor.execute {
                apiClientProvider.client.delete(
                    urlString = FavoritesEndPoint,
                ) { setBody(items) }
            }

    private companion object {

        const val FavoritesEndPoint = "account/users/me/favorites"
    }
}
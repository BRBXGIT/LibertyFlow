package com.brbx.network.account_user.lists.collections.api

import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AccountUserCollectionsApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserCollectionsApi {

    override suspend fun addToCollection(items: List<UserListItem.Collection>): RequestResult<Unit> =
        apiCallExecutor.execute {
            apiClientProvider.client.post(urlString = CollectionsEndPoint) {
                setBody(items)
            }
        }

    override suspend fun deleteFromCollection(items: List<UserListItem.Collection>):
        RequestResult<Unit> =
            apiCallExecutor.execute {
                apiClientProvider.client.delete(urlString = CollectionsEndPoint) {
                    setBody(items)
                }
            }

    private companion object {

        const val CollectionsEndPoint = "accounts/users/me/collections"
    }
}
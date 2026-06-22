package com.brbx.network.account_user.lists.collections.collection_ids.api

import com.brbx.network.account_user.lists.collections.model.CollectionIds
import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class AccountUserCollectionsIdsApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserCollectionsIdsApi {

    override suspend fun getIds(): RequestResult<List<CollectionIds>> =
        apiCallExecutor.execute {
            apiClientProvider.client.get(urlString = CollectionsIdsEndPoint).body()
        }


    private companion object {
        const val CollectionsIdsEndPoint = "accounts/users/me/collections/ids"
    }
}
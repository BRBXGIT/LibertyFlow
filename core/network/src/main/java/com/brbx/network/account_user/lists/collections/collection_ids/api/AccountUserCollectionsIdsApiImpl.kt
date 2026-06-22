package com.brbx.network.account_user.lists.collections.collection_ids.api

import com.brbx.network.account_user.lists.collections.model.CollectionsIds
import com.brbx.network.account_user.lists.collections.model.CollectionsIdsSerializer
import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal class AccountUserCollectionsIdsApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserCollectionsIdsApi {

    override suspend fun getIds(): RequestResult<CollectionsIds> =
        apiCallExecutor.execute {
            val result = apiClientProvider.client.get(urlString = CollectionsIdsEndPoint)
            Json.decodeFromString(
                deserializer = CollectionsIdsSerializer,
                string = result.bodyAsText(),
            )
        }


    private companion object {
        const val CollectionsIdsEndPoint = "accounts/users/me/collections/ids"
    }
}
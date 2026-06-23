package com.brbx.network.account_user.lists.collections.collections.api

import com.brbx.network.account_user.lists.collections.model.CollectionsIds
import com.brbx.network.account_user.lists.collections.model.CollectionsIdsSerializer
import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.executor.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal class AccountUserCollectionsApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserCollectionsApi {

    override suspend fun addToCollection(
        items: List<UserListItem.Collection>
    ): RequestResult<CollectionsIds> = apiCallExecutor.execute {
        val result = apiClientProvider.client.post(urlString = CollectionsEndPoint) {
            setBody(items)
        }
        Json.decodeFromString(
            deserializer = CollectionsIdsSerializer,
            string = result.bodyAsText(),
        )
    }

    override suspend fun deleteFromCollection(
        items: List<UserListItem.Collection>
    ): RequestResult<CollectionsIds> = apiCallExecutor.execute {
        val result = apiClientProvider.client.delete(urlString = CollectionsEndPoint) {
            setBody(items)
        }
        Json.decodeFromString(
            deserializer = CollectionsIdsSerializer,
            string = result.bodyAsText(),
        )
    }

    private companion object {
        const val CollectionsEndPoint = "accounts/users/me/collections"
    }
}
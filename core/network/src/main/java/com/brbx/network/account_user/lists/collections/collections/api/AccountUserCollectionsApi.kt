package com.brbx.network.account_user.lists.collections.collections.api

import com.brbx.network.account_user.lists.collections.model.CollectionIds
import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.model.result.RequestResult

interface AccountUserCollectionsApi {

    suspend fun addToCollection(
        items: List<UserListItem.Collection>
    ): RequestResult<List<CollectionIds>>

    suspend fun deleteFromCollection(
        items: List<UserListItem.Collection>
    ): RequestResult<List<CollectionIds>>
}
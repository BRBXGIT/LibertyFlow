package com.brbx.network.account_user.lists.collections.collections.api

import com.brbx.network.account_user.lists.collections.model.CollectionsIds
import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.model.result.RequestResult

interface AccountUserCollectionsApi {

    suspend fun addToCollection(
        items: List<UserListItem.Collection>
    ): RequestResult<CollectionsIds>

    suspend fun deleteFromCollection(
        items: List<UserListItem.Collection>
    ): RequestResult<CollectionsIds>
}
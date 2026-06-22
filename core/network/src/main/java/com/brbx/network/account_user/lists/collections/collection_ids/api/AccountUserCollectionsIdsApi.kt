package com.brbx.network.account_user.lists.collections.collection_ids.api

import com.brbx.network.account_user.lists.collections.model.CollectionIds
import com.brbx.network.base.model.result.RequestResult

interface AccountUserCollectionsIdsApi {

    suspend fun getIds(): RequestResult<List<CollectionIds>>
}
package com.brbx.network.account_user.lists.collection_ids.api

import com.brbx.network.account_user.lists.collection_ids.model.CollectionIds
import com.brbx.network.base.model.result.RequestResult

interface AccountUserCollectionsIdsApi {

    suspend fun getIds(): RequestResult<List<CollectionIds>>
}
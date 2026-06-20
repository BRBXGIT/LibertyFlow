package com.brbx.network.account_user.lists.collections.collection_ids.api

import com.brbx.network.account_user.lists.collections.collection_ids.model.CollectionsIds
import com.brbx.network.base.model.result.RequestResult

interface AccountUserCollectionsIdsApi {

    suspend fun getIds(): RequestResult<List<CollectionsIds>>
}
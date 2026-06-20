package com.brbx.network.account_user.lists.favorites_ids.api

import com.brbx.network.base.model.result.RequestResult

interface AccountUserFavoritesIdsApi {

    suspend fun getIds(): RequestResult<List<Int>>
}
package com.brbx.network.account_user.lists.favorites.favorites_ids.api

import com.brbx.network.account_user.lists.favorites.model.FavoritesIds
import com.brbx.network.base.model.result.RequestResult

interface AccountUserFavoritesIdsApi {

    suspend fun getIds(): RequestResult<FavoritesIds>
}
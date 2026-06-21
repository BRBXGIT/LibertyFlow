package com.brbx.network.account_user.lists.favorites.favorites.api

import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.model.result.RequestResult

interface AccountUserFavoritesApi {

    suspend fun addToFavorites(items: List<UserListItem.Favorite>): RequestResult<List<Int>>

    suspend fun deleteFromFavorites(items: List<UserListItem.Favorite>): RequestResult<List<Int>>
}
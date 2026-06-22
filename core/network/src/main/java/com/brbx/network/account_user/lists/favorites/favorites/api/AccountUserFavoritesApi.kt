package com.brbx.network.account_user.lists.favorites.favorites.api

import com.brbx.network.account_user.lists.favorites.model.FavoritesIds
import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.model.result.RequestResult

interface AccountUserFavoritesApi {

    suspend fun addToFavorites(items: List<UserListItem.Favorite>): RequestResult<FavoritesIds>

    suspend fun deleteFromFavorites(items: List<UserListItem.Favorite>): RequestResult<FavoritesIds>
}
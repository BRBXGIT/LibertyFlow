package com.brbx.domain.network.user.lists.favorites.favorites.repository

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.model.DomainUserListItem

interface UserFavoritesRepository {

    suspend fun addToFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit>

    suspend fun deleteFromFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit>
}
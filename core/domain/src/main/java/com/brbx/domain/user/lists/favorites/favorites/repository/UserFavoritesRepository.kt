package com.brbx.domain.user.lists.favorites.favorites.repository

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.model.DomainUserListItem

interface UserFavoritesRepository {

    suspend fun addToFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit>

    suspend fun deleteFromFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit>
}
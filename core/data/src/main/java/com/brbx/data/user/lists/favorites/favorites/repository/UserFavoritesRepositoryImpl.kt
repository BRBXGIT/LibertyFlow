package com.brbx.data.user.lists.favorites.favorites.repository

import com.brbx.data.common.map.toDomain
import com.brbx.data.user.lists.favorites.interactor.FavoritesIdsInteractor
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.favorites.favorites.repository.UserFavoritesRepository
import com.brbx.domain.user.lists.model.DomainUserListItem
import com.brbx.network.account_user.lists.favorites.favorites.api.AccountUserFavoritesApi
import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.model.result.RequestResult

internal class UserFavoritesRepositoryImpl(
    private val interactor: FavoritesIdsInteractor,
    private val api: AccountUserFavoritesApi,
) : UserFavoritesRepository {

    override suspend fun addToFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit> = executeFavoriteAction(items) { dataItems ->
        api.addToFavorites(dataItems)
    }

    override suspend fun deleteFromFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit> = executeFavoriteAction(items) { dataItems ->
        api.deleteFromFavorites(dataItems)
    }

    private suspend fun executeFavoriteAction(
        items: List<DomainUserListItem.Favorite>,
        apiCall: suspend (List<UserListItem.Favorite>) -> RequestResult<List<Int>>
    ): DomainRequestResult<Unit> {
        val dataItems = items.map { it.toData() }
        val result = apiCall(dataItems).toDomain { it }

        return when (result) {
            is DomainRequestResult.Success -> {
                interactor.update(ids = result.data)
                DomainRequestResult.Success(data = Unit)
            }
            is DomainRequestResult.Error -> result
        }
    }

    private fun DomainUserListItem.Favorite.toData(): UserListItem.Favorite =
        UserListItem.Favorite(id = this.id)
}
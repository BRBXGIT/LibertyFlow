package com.brbx.data.network.user.lists.favorites.favorites.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.user.lists.favorites.interactor.FavoritesIdsInteractor
import com.brbx.data.network.user.lists.favorites.map.toDomain
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.favorites.favorites.repository.UserFavoritesRepository
import com.brbx.domain.network.user.lists.favorites.model.DomainFavoritesIds
import com.brbx.domain.network.user.lists.model.DomainUserListItem
import com.brbx.network.account_user.lists.favorites.favorites.api.AccountUserFavoritesApi
import com.brbx.network.account_user.lists.model.UserListItem

internal class UserFavoritesRepositoryImpl(
    private val interactor: FavoritesIdsInteractor,
    private val api: AccountUserFavoritesApi,
    private val executor: ApiCallExecutor,
) : UserFavoritesRepository {

    override suspend fun addToFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit> = executeFavoriteAction(items) { dataItems ->
        executor.execute(mapper = { it.toDomain() }) { api.addToFavorites(dataItems) }
    }

    override suspend fun deleteFromFavorites(
        items: List<DomainUserListItem.Favorite>
    ): DomainRequestResult<Unit> = executeFavoriteAction(items) { dataItems ->
        executor.execute(mapper = { it.toDomain() }) { api.deleteFromFavorites(dataItems) }
    }

    private suspend fun executeFavoriteAction(
        items: List<DomainUserListItem.Favorite>,
        apiCall: suspend (List<UserListItem.Favorite>) -> DomainRequestResult<DomainFavoritesIds>
    ): DomainRequestResult<Unit> {
        val dataItems = items.map { it.toData() }
        return when (val result = apiCall(dataItems)) {
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
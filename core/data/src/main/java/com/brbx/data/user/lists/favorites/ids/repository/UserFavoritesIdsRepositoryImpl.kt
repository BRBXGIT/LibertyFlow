package com.brbx.data.user.lists.favorites.ids.repository

import com.brbx.data.common.map.toDomain
import com.brbx.data.user.lists.favorites.interactor.FavoritesIdsInteractor
import com.brbx.data.user.lists.favorites.interactor.FavoritesIdsSource
import com.brbx.data.user.lists.favorites.map.toDomain
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.favorites.favorites_ids.repository.UserFavoritesIdsRepository
import com.brbx.domain.user.lists.favorites.model.DomainFavoritesIds
import com.brbx.network.account_user.lists.favorites.favorites_ids.api.AccountUserFavoritesIdsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class UserFavoritesIdsRepositoryImpl(
    private val interactor: FavoritesIdsInteractor,
    private val source: FavoritesIdsSource,
    private val api: AccountUserFavoritesIdsApi,
) : UserFavoritesIdsRepository {

    override fun getIds(): Flow<DomainRequestResult<DomainFavoritesIds>> =
        flow {
            if (source.ids.value == null) {
                val result = api.getIds().toDomain { it.toDomain() }
                when (result) {
                    is DomainRequestResult.Success -> interactor.update(ids = result.data)
                    is DomainRequestResult.Error -> {
                        emit(value = result)
                        return@flow
                    }
                }
            }
            source.ids.collect { cachedIds ->
                if (cachedIds != null) {
                    emit(value = DomainRequestResult.Success(data = cachedIds))
                }
            }
        }
}
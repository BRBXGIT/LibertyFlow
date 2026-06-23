package com.brbx.domain.network.user.lists.favorites.favorites_ids.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.favorites.favorites_ids.repository.UserFavoritesIdsRepository
import com.brbx.domain.network.user.lists.favorites.model.DomainFavoritesIds
import kotlinx.coroutines.flow.Flow

class GetUserFavoritesIdsUseCase(
    private val repository: UserFavoritesIdsRepository,
) {
    operator fun invoke(): Flow<DomainRequestResult<DomainFavoritesIds>> =
        repository.getIds()
}
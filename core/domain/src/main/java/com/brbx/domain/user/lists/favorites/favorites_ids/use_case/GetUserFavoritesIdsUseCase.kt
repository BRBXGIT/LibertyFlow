package com.brbx.domain.user.lists.favorites.favorites_ids.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.favorites.favorites_ids.repository.UserFavoritesIdsRepository
import kotlinx.coroutines.flow.Flow

class GetUserFavoritesIdsUseCase(
    private val repository: UserFavoritesIdsRepository,
) {
    operator fun invoke(): Flow<DomainRequestResult<List<Int>>> =
        repository.getIds()
}
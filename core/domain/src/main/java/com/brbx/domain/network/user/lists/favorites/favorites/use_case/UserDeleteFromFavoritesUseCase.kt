package com.brbx.domain.network.user.lists.favorites.favorites.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.favorites.favorites.map.toDomain
import com.brbx.domain.network.user.lists.favorites.favorites.repository.UserFavoritesRepository

class UserDeleteFromFavoritesUseCase(
    private val repository: UserFavoritesRepository,
) {
    suspend operator fun invoke(items: List<Int>): DomainRequestResult<Unit> {
        val domainItems = items.toDomain()
        return repository.deleteFromFavorites(domainItems)
    }
}
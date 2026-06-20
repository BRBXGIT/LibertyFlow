package com.brbx.domain.user.lists.collections.collections.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.collections.model.CollectionItem
import com.brbx.domain.user.lists.collections.collections.repository.UserCollectionsRepository

class UserAddToCollectionUseCase(
    private val repository: UserCollectionsRepository,
) {
    suspend operator fun invoke(
        items: List<CollectionItem>,
    ): DomainRequestResult<Unit> {
        val domainItems = items.map { it.toDomain() }
        return repository.addToCollection(domainItems)
    }
}
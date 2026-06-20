package com.brbx.domain.user.lists.collections.collections_ids.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.collections_ids.model.CollectionIds
import com.brbx.domain.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import kotlinx.coroutines.flow.Flow

class GetUserCollectionsIdsUseCase(
    private val repository: UserCollectionsIdsRepository,
) {
    operator fun invoke(): Flow<DomainRequestResult<List<CollectionIds>>> =
        repository.getIds()
}
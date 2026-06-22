package com.brbx.domain.user.lists.collections.collections_ids.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import com.brbx.domain.user.lists.collections.model.DomainCollectionIds
import kotlinx.coroutines.flow.Flow

class GetUserCollectionsIdsUseCase(
    private val repository: UserCollectionsIdsRepository,
) {
    operator fun invoke(): Flow<DomainRequestResult<List<DomainCollectionIds>>> =
        repository.getIds()
}
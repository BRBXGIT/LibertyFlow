package com.brbx.domain.network.user.lists.collections.collections_ids.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import com.brbx.domain.network.user.lists.collections.model.DomainCollectionsIds
import kotlinx.coroutines.flow.Flow

class GetUserCollectionsIdsUseCase(
    private val repository: UserCollectionsIdsRepository,
) {
    operator fun invoke(): Flow<DomainRequestResult<DomainCollectionsIds>> =
        repository.getIds()
}
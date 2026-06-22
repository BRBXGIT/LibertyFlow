package com.brbx.domain.user.lists.collections.collections_ids.repository

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.model.DomainCollectionIds
import kotlinx.coroutines.flow.Flow

interface UserCollectionsIdsRepository {

    fun getIds(): Flow<DomainRequestResult<List<DomainCollectionIds>>>
}
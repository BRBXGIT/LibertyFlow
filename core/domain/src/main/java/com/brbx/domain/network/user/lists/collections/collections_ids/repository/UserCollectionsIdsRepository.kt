package com.brbx.domain.network.user.lists.collections.collections_ids.repository

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.collections.model.DomainCollectionsIds
import kotlinx.coroutines.flow.Flow

interface UserCollectionsIdsRepository {

    fun getIds(): Flow<DomainRequestResult<DomainCollectionsIds>>
}
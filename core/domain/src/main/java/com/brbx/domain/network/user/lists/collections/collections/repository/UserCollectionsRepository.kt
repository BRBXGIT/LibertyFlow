package com.brbx.domain.network.user.lists.collections.collections.repository

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.model.DomainUserListItem

interface UserCollectionsRepository {

    suspend fun addToCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit>

    suspend fun deleteFromCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit>
}
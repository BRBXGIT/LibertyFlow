package com.brbx.domain.user.lists.collections.collections.repository

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.model.DomainUserListItem

interface UserCollectionsRepository {

    suspend fun addToCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit>

    suspend fun deleteFromCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit>
}
package com.brbx.domain.user.lists.collections.collections.model

import com.brbx.domain.model.common.Collection
import com.brbx.domain.user.lists.model.DomainUserListItem

data class CollectionItem(
    val id: Int,
    val collection: Collection,
) {
    fun toDomain(): DomainUserListItem.Collection =
        DomainUserListItem.Collection(
            id = this.id,
            collection = this.collection,
        )
}
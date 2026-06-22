package com.brbx.data.user.lists.collections.map

import com.brbx.data.common.map.toCollection
import com.brbx.domain.user.lists.collections.model.DomainCollectionIds
import com.brbx.network.account_user.lists.collections.model.CollectionIds

internal fun CollectionIds.toDomain(): DomainCollectionIds =
    DomainCollectionIds(
        ids = this.ids,
        collection = this.collection.toCollection(),
    )
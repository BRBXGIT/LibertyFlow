package com.brbx.data.user.lists.collections.map

import com.brbx.data.common.map.toCollection
import com.brbx.domain.user.lists.collections.model.DomainCollectionsIds
import com.brbx.network.account_user.lists.collections.model.CollectionsIds

internal fun CollectionsIds.toDomain(): DomainCollectionsIds =
    this.mapKeys { (key, _) -> key.toCollection() }
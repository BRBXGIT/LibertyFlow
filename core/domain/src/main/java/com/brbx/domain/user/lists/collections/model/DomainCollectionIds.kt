package com.brbx.domain.user.lists.collections.model

import com.brbx.domain.model.common.Collection

data class DomainCollectionIds(
    val ids: List<Int>,
    val collection: Collection,
)
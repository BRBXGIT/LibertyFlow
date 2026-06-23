package com.brbx.domain.network.user.lists.model

import com.brbx.domain.network.model.common.DomainCollection

sealed interface DomainUserListItem {
    val id: Int

    @JvmInline
    value class Favorite(override val id: Int) : DomainUserListItem

    data class Collection(
        override val id: Int,
        val collection: DomainCollection,
    ) : DomainUserListItem
}
package com.brbx.domain.user.lists.favorites.favorites.map

import com.brbx.domain.model.common.Collection
import com.brbx.domain.user.lists.model.DomainUserListItem

internal fun List<Int>.toDomain(): List<DomainUserListItem.Favorite> =
    this.map { DomainUserListItem.Favorite(id = it) }

internal fun List<Int>.toDomain(collection: Collection): List<DomainUserListItem.Collection> =
    this.map { DomainUserListItem.Collection(id = it, collection = collection) }
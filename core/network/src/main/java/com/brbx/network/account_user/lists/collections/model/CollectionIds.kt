package com.brbx.network.account_user.lists.collections.model

import kotlinx.serialization.Serializable

@Serializable(with = CollectionsIdsSerializer::class)
data class CollectionIds(
    val ids: List<Int>,
    val collection: String,
)
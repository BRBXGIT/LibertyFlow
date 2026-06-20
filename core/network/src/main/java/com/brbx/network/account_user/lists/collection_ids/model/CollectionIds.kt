package com.brbx.network.account_user.lists.collection_ids.model

import kotlinx.serialization.Serializable

@Serializable(with = CollectionIdsSerializer::class)
data class CollectionIds(
    val ids: List<Int>,
    val status: String,
)
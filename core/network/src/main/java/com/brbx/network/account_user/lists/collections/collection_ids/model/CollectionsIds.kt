package com.brbx.network.account_user.lists.collections.collection_ids.model

import kotlinx.serialization.Serializable

@Serializable(with = CollectionsIdsSerializer::class)
data class CollectionsIds(
    val ids: List<Int>,
    val status: String,
)
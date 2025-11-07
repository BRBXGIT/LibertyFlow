package com.example.data.models.collections

import com.example.network.collections.models.CollectionItem
import com.example.network.collections.models.CollectionRequest

internal fun UiCollectionItem.toCollectionItem(): CollectionItem {
    return CollectionItem(
        id = id,
        collectionType = collectionType.name
    )
}

internal fun UiCollectionRequest.toCollectionRequest(): CollectionRequest {
    val collectionRequest = CollectionRequest()
    collectionRequest.addAll(this.map { it.toCollectionItem() })
    return collectionRequest
}
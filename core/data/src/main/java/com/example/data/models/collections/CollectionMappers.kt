package com.example.data.models.collections

import com.example.network.collections.models.CollectionItem
import com.example.network.collections.models.CollectionRequest

internal fun UiCollectionItem.toCollectionItem(): CollectionItem {
    return CollectionItem(
        releaseId = id,
        collectionType = collection.name
    )
}

internal fun UiCollectionRequest.toCollectionRequest(): CollectionRequest {
    val collectionRequest = CollectionRequest()
    collectionRequest.addAll(this.map { it.toCollectionItem() })
    return collectionRequest
}
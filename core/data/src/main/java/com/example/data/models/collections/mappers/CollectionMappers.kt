package com.example.data.models.collections.mappers

import com.example.data.models.collections.collection_ids.UiCollectionIds
import com.example.data.models.collections.collection_ids.UiCollectionIdsItem
import com.example.data.models.collections.request.UiCollectionItem
import com.example.data.models.collections.request.UiCollectionRequest
import com.example.network.collections.models.ids.CollectionsIds
import com.example.network.collections.models.ids.CollectionsIdsSubList
import com.example.network.collections.models.request.CollectionItem
import com.example.network.collections.models.request.CollectionRequest

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

internal fun CollectionsIds.toUiCollectionIds(): UiCollectionIds {
    val collections = UiCollectionIds()
    collections.addAll(this.map { it.toUiCollectionIdItem() })
    return collections
}

internal fun CollectionsIdsSubList.toUiCollectionIdItem() =
    UiCollectionIdsItem().also { it.addAll(this) }
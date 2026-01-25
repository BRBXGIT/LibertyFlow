package com.example.data.models.collections.mappers

import com.example.data.models.collections.collection_ids.CollectionIds
import com.example.data.models.collections.collection_ids.CollectionIdsItem
import com.example.data.models.collections.request.CollectionItem
import com.example.data.models.collections.request.CollectionRequest
import com.example.network.collections.models.ids.CollectionsIdsDto
import com.example.network.collections.models.ids.CollectionsIdsSubListDto
import com.example.network.collections.models.request.CollectionItemDto
import com.example.network.collections.models.request.CollectionRequestDto

internal fun CollectionItem.toCollectionItemDto(): CollectionItemDto {
    return CollectionItemDto(
        releaseId = id,
        collectionType = collection.name
    )
}

internal fun CollectionRequest.toCollectionRequestDto(): CollectionRequestDto {
    val collectionRequestDto = CollectionRequestDto()
    collectionRequestDto.addAll(this.map { it.toCollectionItemDto() })
    return collectionRequestDto
}

internal fun CollectionsIdsDto.toCollectionIds(): CollectionIds {
    val collections = CollectionIds()
    collections.addAll(this.map { it.toCollectionIdItem() })
    return collections
}

internal fun CollectionsIdsSubListDto.toCollectionIdItem() =
    CollectionIdsItem().also { it.addAll(this) }
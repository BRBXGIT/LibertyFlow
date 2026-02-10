package com.example.data.models.collections.mappers

import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.collections.request.CollectionItem
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.common.request.request_parameters.Collection
import com.example.network.collections.models.ids.CollectionsIdsDto
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

internal fun CollectionsIdsDto.toAnimeCollections(): List<AnimeCollection> {
    return this.mapNotNull { subList ->
        val rawType = subList.lastOrNull()?.toString()?.uppercase()

        val collectionType = Collection.entries.find { it.name == rawType }

        if (collectionType == null) return@mapNotNull null

        val ids = subList.dropLast(1).mapNotNull { it as? Int }

        AnimeCollection(collection = collectionType, ids = ids)
    }
}
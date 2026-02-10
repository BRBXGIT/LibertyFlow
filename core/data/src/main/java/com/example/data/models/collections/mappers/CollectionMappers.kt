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

internal fun CollectionsIdsDto.toAnimeCollection(): List<AnimeCollection> {
    return this.mapNotNull { item ->
        if (item.size < 2) return@mapNotNull null

        val id = when (val idValue = item[0]) {
            is Number -> idValue.toInt()
            is String -> idValue.toIntOrNull()
            else -> null
        }

        val status = try {
            Collection.valueOf(item[1].toString())
        } catch (e: IllegalArgumentException) {
            null
        }

        if (id != null && status != null) {
            AnimeCollection(status, id)
        } else {
            null
        }
    }
}
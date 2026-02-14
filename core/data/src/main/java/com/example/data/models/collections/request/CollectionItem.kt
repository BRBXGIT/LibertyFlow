package com.example.data.models.collections.request

import com.example.data.models.common.request.request_parameters.Collection

/**
 * Represents an individual item belonging to an anime collection.
 *
 * @property id The unique identifier for this specific item.
 * @property collection The parent [Collection] associated with this item.
 */
data class CollectionItem(
    val id: Int,
    val collection: Collection
)

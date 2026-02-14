package com.example.data.models.collections.collection

import androidx.compose.runtime.Immutable
import com.example.data.models.common.request.request_parameters.Collection

/**
 * Represents an immutable collection of anime entries.
 * * This class is marked with [@Immutable] to indicate that all public properties
 * will not change after the object is constructed, allowing for UI
 * optimizations like skipping recomposition.
 *
 * @property collection The underlying [Collection] containing the anime data.
 * @property id A unique identifier for this specific anime collection.
 */
@Immutable
data class AnimeCollection(
    val collection: Collection,
    val id: Int
)

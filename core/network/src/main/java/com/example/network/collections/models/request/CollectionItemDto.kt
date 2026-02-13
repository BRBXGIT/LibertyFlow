package com.example.network.collections.models.request

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a specific anime release within a user's collection.
 * @property releaseId The unique identifier of the anime title.
 * @property collectionType The category the title belongs to (e.g., 'WATCHING', 'DROPPED', 'PLANNED').
 */
data class CollectionItemDto(
    @field:SerializedName(FIELD_RELEASE_ID)
    val releaseId: Int,

    @field:SerializedName(FIELD_COLLECTION_TYPE)
    val collectionType: String
) {
    companion object Fields {
        private const val FIELD_RELEASE_ID = "release_id"
        private const val FIELD_COLLECTION_TYPE = "type_of_collection"
    }
}
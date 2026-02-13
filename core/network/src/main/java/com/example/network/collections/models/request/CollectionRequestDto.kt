package com.example.network.collections.models.request

/**
 * Data Transfer Object representing a list of anime collection items.
 * * This class extends [ArrayList] of [CollectionItemDto], allowing it to be serialized
 * as a JSON array of collection items (e.g., for bulk add/delete operations).
 */
class CollectionRequestDto : ArrayList<CollectionItemDto>()
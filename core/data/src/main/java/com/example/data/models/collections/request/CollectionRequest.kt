package com.example.data.models.collections.request

/**
 * A specialized [ArrayList] representing a request payload of [CollectionItem]s.
 * * This class facilitates the grouping of multiple collection items for
 * bulk operations, such as API submissions or batch processing.
 */
class CollectionRequest: ArrayList<CollectionItem>()
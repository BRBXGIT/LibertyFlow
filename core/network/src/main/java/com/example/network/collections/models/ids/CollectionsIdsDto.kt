package com.example.network.collections.models.ids

/**
 * Data Transfer Object representing the collection of all user category IDs.
 * Matches the nested list structure returned by the accounts/collections/ids endpoint.
 *
 * @description This class extends ArrayList of [CollectionsIdItem],
 * effectively creating a List<List<Any>> structure.
 */
class CollectionsIdsDto : ArrayList<CollectionsIdItem>()
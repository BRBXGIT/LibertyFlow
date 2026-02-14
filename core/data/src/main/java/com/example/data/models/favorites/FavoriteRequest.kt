package com.example.data.models.favorites

/**
 * A specialized [ArrayList] of [FavoriteItem]s.
 *
 * This collection is generally used as a request body when
 * performing bulk operations on a user's favorites list.
 */
class FavoriteRequest: ArrayList<FavoriteItem>()
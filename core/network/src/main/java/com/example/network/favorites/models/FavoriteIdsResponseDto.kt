package com.example.network.favorites.models

/**
 * Response DTO containing a flat list of release IDs that the user has favorited.
 * Typically used for quick lookups to update UI states (like heart icons).
 */
class FavoriteIdsResponseDto : ArrayList<Int>()
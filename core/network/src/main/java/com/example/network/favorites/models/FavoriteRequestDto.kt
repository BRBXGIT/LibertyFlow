package com.example.network.favorites.models

/**
 * Request DTO used for bulk operations on favorites.
 * Extends [ArrayList] of [FavoriteAnimeIdItemDto] to be serialized as a JSON array.
 */
class FavoriteRequestDto : ArrayList<FavoriteAnimeIdItemDto>()
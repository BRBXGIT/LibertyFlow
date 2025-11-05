package com.example.data.models.favorites

import com.example.network.favorites.models.FavoriteAnimeIdItem
import com.example.network.favorites.models.FavoriteRequest

internal fun UiFavoriteItem.toFavoriteAnimeIdItem() = FavoriteAnimeIdItem(id)

internal fun UiFavoriteRequest.toFavoriteRequest(): FavoriteRequest {
    val favoriteRequest = FavoriteRequest()
    favoriteRequest.addAll(this.map { it.toFavoriteAnimeIdItem() })
    return favoriteRequest
}
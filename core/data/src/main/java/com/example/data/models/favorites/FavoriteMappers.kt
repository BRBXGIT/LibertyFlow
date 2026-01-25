package com.example.data.models.favorites

import com.example.network.favorites.models.FavoriteAnimeIdItemDto
import com.example.network.favorites.models.FavoriteIdsResponseDto
import com.example.network.favorites.models.FavoriteRequestDto

internal fun FavoriteItem.toFavoriteAnimeIdItemDto() = FavoriteAnimeIdItemDto(id)

internal fun FavoriteRequest.toFavoriteRequestDto(): FavoriteRequestDto {
    val favoriteRequestDto = FavoriteRequestDto()
    favoriteRequestDto.addAll(this.map { it.toFavoriteAnimeIdItemDto() })
    return favoriteRequestDto
}

internal fun FavoriteIdsResponseDto.toFavoritesIds() =
    FavoritesIds().also { it.addAll(this) }

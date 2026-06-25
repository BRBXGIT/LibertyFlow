package com.brbx.common.model.anime_item.map

import com.brbx.common.model.anime_item.model.AnimeItem
import com.brbx.common.model.common.map.toUi
import com.brbx.domain.network.model.response.common.DomainAnimeItem

fun DomainAnimeItem.toUi(): AnimeItem =
    AnimeItem(
        favoritesCount = this.favoritesCount,
        genres = this.genres.map { it.toUi() },
        id = this.id,
        name = this.name.toUi(),
        poster = this.poster.toUi(),
    )
package com.example.data.models.common.mappers

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.network.common.common_response_models.AnimeResponseItem
import com.example.network.common.common_response_models.Genre
import com.example.network.common.common_response_models.Name
import com.example.network.common.common_response_models.Poster

fun AnimeResponseItem.toUiAnimeItem(): UiAnimeItem {
    return UiAnimeItem(
        id = id,
        genres = genres.map { it.toUiGenre() },
        poster = poster.toUiPoster(),
        name = name.toUiName()
    )
}

fun Genre.toUiGenre(): UiGenre {
    return UiGenre(
        id = id,
        name = name
    )
}

fun Poster.toUiPoster(): UiPoster {
    return UiPoster(
        thumbnail = thumbnail,
        preview = preview,
        src = src
    )
}

fun Name.toUiName(): UiName {
    return UiName(
        russian = main,
        english = english,
        alternative = alternative
    )
}
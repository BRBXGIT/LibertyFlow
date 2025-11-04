package com.example.data.models.common.ui_anime_item

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster
import com.example.data.models.common.ui_anime_base.UiAnimeBase

data class UiAnimeItem(
    override val id: Int,

    val genres: List<UiGenre>,
    val poster: UiPoster,
    val name: UiName
): UiAnimeBase(id) {
    fun genresAsString() = genres.joinToString(", ") { it.name }
}
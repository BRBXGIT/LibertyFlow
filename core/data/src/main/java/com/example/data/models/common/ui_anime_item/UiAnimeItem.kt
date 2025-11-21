package com.example.data.models.common.ui_anime_item

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster

@Immutable
data class UiAnimeItem(
    val id: Int,
    val genres: List<UiGenre>,
    val poster: UiPoster,
    val name: UiName
) {
    fun genresAsString() = genres.joinToString(" | ") { it.name }
}
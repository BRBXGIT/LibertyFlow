package com.example.data.models.common.ui_anime_item

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.Genre
import com.example.data.models.common.common.Name
import com.example.data.models.common.common.Poster

@Immutable
data class AnimeItem(
    val id: Int,
    val genres: List<Genre>,
    val poster: Poster,
    val name: Name
) {
    fun genresAsString() = genres.joinToString(" | ") { it.name }
}
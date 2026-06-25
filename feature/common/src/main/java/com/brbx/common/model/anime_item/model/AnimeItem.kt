package com.brbx.common.model.anime_item.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Name
import com.brbx.common.model.common.model.Poster

@Immutable
@optics
data class AnimeItem(
    val favoritesCount: Int,
    val genres: List<Genre>,
    val id: Int,
    val name: Name,
    val poster: Poster,
) {
    fun genresAsString(): String =
        this.genres.joinToString(separator = " | ")

    companion object
}
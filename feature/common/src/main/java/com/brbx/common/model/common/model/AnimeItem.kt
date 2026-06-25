package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics

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
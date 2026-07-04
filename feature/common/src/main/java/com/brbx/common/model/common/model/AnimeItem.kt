package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText

@Immutable
@optics
data class AnimeItem(
    val genres: List<Genre>,
    val id: Int,
    val name: Name,
    val posterPath: Poster,
) {
    fun genresAsBrbxText(): BrbxText = genres
        .joinToString(separator = " | ") { genre -> genre.name }
        .toBrbxText()

    companion object
}

package com.brbx.design_system.component.anime_card

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.ui_compose.common.BrbxText

@Immutable
@optics
data class AnimeCardModel(
    val genres: List<String>,
    val id: Int,
    val name: BrbxText,
    val fullPosterPath: String,
) {
    fun genresAsString(): String = genres.joinToString(separator = " | ")

    companion object
}
package com.example.data.models.common.anime_item

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.Genre
import com.example.data.models.common.common.Name
import com.example.data.models.common.common.Poster

private const val GENRE_DIVIDER = " | "

/**
 * A comprehensive model representing a single anime entry.
 *
 * This class serves as the primary data holder for UI components, aggregating
 * localized names, visual assets, and categorical tags.
 *
 * @property id The unique identifier for the anime.
 * @property genres A list of [Genre] tags associated with this entry.
 * @property poster The [Poster] object containing image paths for different resolutions.
 * @property name The [Name] object containing localized (EN/RU) and alternative titles.
 */
@Immutable
data class AnimeItem(
    val id: Int,
    val genres: List<Genre>,
    val poster: Poster,
    val name: Name
) {
    fun genresAsString() = genres.joinToString(GENRE_DIVIDER) { it.name }
}
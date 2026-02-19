package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.common.vm_helpers.filters.models.FiltersState
import com.example.common.vm_helpers.models.LoadingState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.FullRequestParameters

/**
 * Represents the complete UI state for the Home screen.
 * * This class is [Immutable], making it optimized for Jetpack Compose recomposition.
 * It follows a nested state pattern to separate concerns between general UI visibility,
 * search parameters, and domain-specific data like genres.
 *
 * @property loadingState Global loading and error indicators for the primary paging list.
 * @property randomAnimeState Specialized state for the "Random Anime" feature (loading/error).
 * @property filtersState Encapsulates the visibility of the filter bottom sheet and
 * the actual [FullRequestParameters] used for queries.
 * @property genresState Holds the list of available genres and their independent loading status.
 */
@Immutable
data class HomeState(
    // Loading paging content
    val loadingState: LoadingState = LoadingState(),

    // Random anime
    val randomAnimeState: LoadingState = LoadingState(),

    // Filters
    val filtersState: FiltersState = FiltersState(),

    // Genres
    val genresState: GenresState = GenresState()
) {
    @Immutable
    data class GenresState(
        val genres: List<Genre> = emptyList(),
        val loadingState: LoadingState = LoadingState()
    ) {
        fun withGenres(genres: List<Genre>) = copy(genres = genres)
    }
}

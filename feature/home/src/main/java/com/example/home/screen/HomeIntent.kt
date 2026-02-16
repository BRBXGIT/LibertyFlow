package com.example.home.screen

import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting

/**
 * Sealed interface representing all possible user actions or system events
 * that can trigger a state change or side effect on the Home screen.
 *
 * These intents are processed by [HomeVM.sendIntent] to update the [HomeState].
 */
sealed interface HomeIntent {

    // UI toggles
    data object ToggleSearching: HomeIntent
    data object ToggleFiltersBottomSheet: HomeIntent

    // Flags
    data class SetLoading(val value: Boolean): HomeIntent
    data class SetError(val value: Boolean): HomeIntent

    // Search
    data class UpdateQuery(val query: String): HomeIntent

    // Filters
    data class AddGenre(val genre: Genre): HomeIntent
    data class RemoveGenre(val genre: Genre): HomeIntent
    data class AddSeason(val season: Season): HomeIntent
    data class RemoveSeason(val season: Season): HomeIntent
    data class UpdateFromYear(val year: Int): HomeIntent
    data class UpdateToYear(val year: Int): HomeIntent
    data class UpdateSorting(val sorting: Sorting): HomeIntent
    data object ToggleIsOngoing: HomeIntent

    // Data
    data object GetRandomAnime: HomeIntent
    data object GetGenres: HomeIntent
}


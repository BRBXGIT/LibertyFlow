package com.example.home.screen

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting

// Intent / Actions coming from the UI to the ViewModel
sealed interface HomeIntent {

    // UI toggles
    data object ToggleSearching: HomeIntent
    data object ToggleFiltersBottomSheet: HomeIntent

    // UI flags
    data class SetLoading(val value: Boolean): HomeIntent
    data class SetError(val value: Boolean): HomeIntent
    data class SetRandomAnimeLoading(val value: Boolean): HomeIntent

    // Search query
    data class UpdateQuery(val query: String): HomeIntent

    // Filters
    data class AddGenre(val genre: UiGenre): HomeIntent
    data class RemoveGenre(val genre: UiGenre): HomeIntent
    data class AddSeason(val season: Season): HomeIntent
    data class RemoveSeason(val season: Season): HomeIntent
    data class UpdateFromYear(val year: Int): HomeIntent
    data class UpdateToYear(val year: Int): HomeIntent
    data class UpdateSorting(val sorting: Sorting): HomeIntent
    data object ToggleIsOngoing: HomeIntent

    // Data ops
    data object GetRandomAnime: HomeIntent
    data object GetGenres: HomeIntent

    // Nav
    data class NavigateToAnimeDetails(val id: Int): HomeIntent
}


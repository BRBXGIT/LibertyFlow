package com.example.home.screen

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.Season

// Intent / Actions coming from the UI to the ViewModel
sealed interface HomeIntent {

    // UI toggles
    data object ToggleSearching : HomeIntent
    data object ToggleFiltersBottomSheet : HomeIntent

    // UI flags
    data class SetLoading(val value: Boolean) : HomeIntent
    data class SetError(val value: Boolean) : HomeIntent

    // Search query
    data class UpdateQuery(val value: String) : HomeIntent

    // Filters
    data class AddGenre(val genre: UiGenre) : HomeIntent
    data class RemoveGenre(val genre: UiGenre) : HomeIntent
    data class AddSeason(val season: Season) : HomeIntent
    data class RemoveSeason(val season: Season) : HomeIntent
    data class UpdateFromYear(val value: Int) : HomeIntent
    data class UpdateToYear(val value: Int) : HomeIntent

    // Data ops
    data object GetRandomAnime : HomeIntent
    data object GetGenres : HomeIntent
}


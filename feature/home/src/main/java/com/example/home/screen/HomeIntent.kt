package com.example.home.screen

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.Season

// Intent / Actions coming from the UI to the ViewModel
sealed interface HomeIntent {
    data object GetRandomAnime: HomeIntent
    data object GetGenres: HomeIntent

    data object UpdateIsSearching: HomeIntent

    data class UpdateIsLoading(val isLoading: Boolean): HomeIntent

    data class UpdateIsError(val isError: Boolean): HomeIntent

    data object UpdateIsFiltersBSVisible: HomeIntent
    data class UpdateQuery(val query: String): HomeIntent
    data class AddGenre(val genre: UiGenre): HomeIntent
    data class RemoveGenre(val genre: UiGenre): HomeIntent
    data class AddSeason(val season: Season): HomeIntent
    data class RemoveSeason(val season: Season): HomeIntent
    data class UpdateFromYear(val year: Int): HomeIntent
    data class UpdateToYear(val year: Int): HomeIntent
}

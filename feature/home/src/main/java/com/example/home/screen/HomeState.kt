package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.FullRequestParameters

@Immutable
data class HomeState(
    val loadingState: LoadingState = LoadingState(),
    val isRandomAnimeLoading: Boolean = false,

    val isSearching: Boolean = false,
    val isFiltersVisible: Boolean = false,

    val request: FullRequestParameters = FullRequestParameters(),

    val genres: List<Genre> = emptyList(),
    val isGenresLoading: Boolean = false
) {

    /* --- UI toggles --- */

    fun toggleSearching() =
        copy(isSearching = !isSearching)

    fun toggleFilters() =
        copy(isFiltersVisible = !isFiltersVisible)

    fun withRandomAnimeLoading(value: Boolean) =
        copy(isRandomAnimeLoading = value)

    fun withGenresLoading(value: Boolean) =
        copy(isGenresLoading = value)

    /* --- Request mutations --- */

    fun updateRequest(block: FullRequestParameters.() -> FullRequestParameters) =
        copy(request = request.block())

    fun updateGenres(genres: List<Genre>) =
        copy(genres = genres)
}

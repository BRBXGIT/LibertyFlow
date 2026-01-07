package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters

@Immutable
data class HomeState(
    val loadingState: LoadingState = LoadingState(),
    val isRandomAnimeLoading: Boolean = false,

    val isSearching: Boolean = false,
    val isFiltersVisible: Boolean = false,

    val request: UiFullRequestParameters = UiFullRequestParameters(),

    val genres: List<UiGenre> = emptyList(),
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

    fun updateRequest(block: UiFullRequestParameters.() -> UiFullRequestParameters) =
        copy(request = request.block())

    fun updateGenres(genres: List<UiGenre>) =
        copy(genres = genres)
}

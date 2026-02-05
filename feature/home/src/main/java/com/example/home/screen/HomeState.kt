package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.FullRequestParameters

@Immutable
data class HomeState(
    // Loading paging content
    val loadingState: LoadingState = LoadingState(),

    // Random anime
    val randomAnimeState: LoadingState = LoadingState(),

    // Search
    val isSearching: Boolean = false,

    // Filters
    val filtersState: FiltersState = FiltersState(),

    // Genres
    val genresState: GenresState = GenresState()
) {
    @Immutable
    data class FiltersState(
        val isFiltersBSVisible: Boolean = false,
        val request: FullRequestParameters = FullRequestParameters()
    ) {
        fun toggleBS() = copy(isFiltersBSVisible = !isFiltersBSVisible)

        fun updateRequest(block: FullRequestParameters.() -> FullRequestParameters) =
            copy(request = request.block())
    }

    @Immutable
    data class GenresState(
        val genres: List<Genre> = emptyList(),
        val loadingState: LoadingState = LoadingState()
    ) {
        fun withGenres(genres: List<Genre>) = copy(genres = genres)
    }

    // Toggles
    fun toggleSearching() = copy(isSearching = !isSearching)
}

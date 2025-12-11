package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters

@Immutable
data class HomeState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val randomAnimeId: Int? = null,
    val isSearching: Boolean = false,
    val isFiltersBSVisible: Boolean = false,

    val request: UiFullRequestParameters = UiFullRequestParameters(),

    val genres: List<UiGenre> = emptyList(),
    val isGenresLoading: Boolean = false
) {
    fun toggleIsSearching() = copy(isSearching = !isSearching)

    fun toggleFiltersBottomSheet() = copy(isFiltersBSVisible = !isFiltersBSVisible)

    fun setLoading(value: Boolean) = copy(isLoading = value)

    fun setError(value: Boolean) = copy(isError = value)

    fun setGenresLoading(value: Boolean) = copy(isGenresLoading = value)

    fun updateQuery(query: String) = copy(request = request.updateSearch(query))

    fun addGenre(genre: UiGenre) = copy(request = request.addGenre(genre))

    fun removeGenre(genre: UiGenre) = copy(request = request.removeGenre(genre))

    fun addSeason(season: Season) = copy(request = request.addSeason(season))

    fun removeSeason(season: Season) = copy(request = request.removeSeason(season))

    fun updateFromYear(year: Int) = copy(request = request.updateYear(from = year))

    fun updateToYear(year: Int) = copy(request = request.updateYear(to = year))

    fun updateSorting(sorting: Sorting) = copy(request = request.updateSorting(sorting))

    fun toggleIsOngoing(publishStatuses: List<PublishStatus>) = copy(request = request.toggleIsOngoing(publishStatuses))
}

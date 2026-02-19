package com.example.common.vm_helpers.filters.delegate

import com.example.common.vm_helpers.filters.models.FiltersState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface FiltersDelegate {

    // Filters
    val filtersState: StateFlow<FiltersState>
    fun observeFilters(scope: CoroutineScope ,onUpdate: (FiltersState) -> Unit)

    // UI Visibility state
    fun toggleIsSearching()
    fun toggleFiltersBS()

    // Filter mutations
    fun updateQuery(query: String)
    fun updateSorting(sorting: Sorting)
    fun toggleIsOngoing()

    // Genre management
    fun addGenre(genre: Genre)
    fun removeGenre(genre: Genre)

    // Season & Year filters
    fun addSeason(season: Season)
    fun removeSeason(season: Season)
    fun updateFromYear(year: Int)
    fun updateToYear(year: Int)
}
package com.example.common.vm_helpers.filters.delegate

import com.example.common.vm_helpers.filters.models.FiltersState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class FiltersDelegateImpl: FiltersDelegate {

    // --- State ---
    private val _filtersState = MutableStateFlow(FiltersState())
    override val filtersState = _filtersState.asStateFlow()

    override fun observeFilters(scope: CoroutineScope, onUpdate: (FiltersState) -> Unit) {
        _filtersState
            .onEach { state -> onUpdate(state) }
            .launchIn(scope)
    }

    // --- Ui ---
    override fun toggleIsSearching() =
        _filtersState.update { it.toggleSearching() }
    override fun toggleFiltersBS() =
        _filtersState.update { it.toggleFiltersBS() }

    // --- Filters ---
    override fun updateQuery(query: String) =
        _filtersState.update { it.copy(requestParameters = it.requestParameters.updateSearch(query)) }

    override fun updateSorting(sorting: Sorting) =
        _filtersState.update { it.copy(requestParameters = it.requestParameters.updateSorting(sorting)) }

    override fun toggleIsOngoing() =
        handleToggleOngoing()

    // --- Genres ---
    override fun addGenre(genre: Genre) =
        _filtersState.update { it.copy(requestParameters = it.requestParameters.addGenre(genre)) }

    override fun removeGenre(genre: Genre) =
        _filtersState.update { it.copy(requestParameters = it.requestParameters.removeGenre(genre)) }

    // --- Seasons ---
    override fun addSeason(season: Season) =
        _filtersState.update { it.copy(requestParameters = it.requestParameters.addSeason(season)) }

    override fun removeSeason(season: Season) =
        _filtersState.update { it.copy(requestParameters = it.requestParameters.removeSeason(season)) }

    // --- Years ---
    override fun updateFromYear(year: Int) =
        updateYears(from = year)

    override fun updateToYear(year: Int) =
        updateYears(to = year)

    // --- Helpers ---
    private fun handleToggleOngoing() {
        val request = filtersState.value.requestParameters

        _filtersState.update { state ->
            val isCurrentlyEmpty = request.publishStatuses.isEmpty()
            state.copy(
                requestParameters = request.copy(
                    publishStatuses = if (isCurrentlyEmpty) listOf(PublishStatus.IS_ONGOING) else emptyList()
                )
            )
        }
    }

    private fun updateYears(from: Int? = null, to: Int? = null) {
        when {
            from != null -> {
                _filtersState.update {
                    it.copy(
                        requestParameters = it.requestParameters.copy(
                            years = it.requestParameters.years.copy(
                                from = from
                            )
                        )
                    )
                }
            }
            to != null -> {
                _filtersState.update {
                    it.copy(
                        requestParameters = it.requestParameters.copy(
                            years = it.requestParameters.years.copy(
                                to = to
                            )
                        )
                    )
                }
            }
        }
    }
}
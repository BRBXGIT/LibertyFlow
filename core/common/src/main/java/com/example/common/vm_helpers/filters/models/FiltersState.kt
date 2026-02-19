package com.example.common.vm_helpers.filters.models

import androidx.compose.runtime.Immutable
import com.example.data.models.common.request.request_parameters.FullRequestParameters

/**
 * Represents the UI state for the filters screen, managing visibility toggles
 * and the underlying data parameters for the search request.
 *
 * @property isFiltersBSVisible Determines if the filters bottom sheet is currently displayed.
 * @property isSearching Indicates if a search operation is currently in progress (e.g., showing a loader).
 * @property requestParameters The current set of criteria used to filter the data.
 */
@Immutable
data class FiltersState(
    // --- Ui ---
    val isFiltersBSVisible: Boolean = false,
    val isSearching: Boolean = false,

    // --- Request ---
    val requestParameters: FullRequestParameters = FullRequestParameters(),
) {
    // Toggles
    fun toggleSearching() = copy(isSearching = !isSearching)
    fun toggleFiltersBS() = copy(isFiltersBSVisible = !isFiltersBSVisible)
}

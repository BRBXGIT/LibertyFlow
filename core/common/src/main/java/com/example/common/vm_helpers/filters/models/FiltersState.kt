package com.example.common.vm_helpers.filters.models

import com.example.data.models.common.request.request_parameters.FullRequestParameters

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

package com.example.common.ui_helpers.search

import androidx.compose.runtime.Immutable

@Immutable
data class SearchForm(
    val query: String = "",
    val isSearching: Boolean = false
) {
    fun toggleSearching() =
        copy(isSearching = !isSearching)

    fun updateQuery(query: String) =
        copy(query = query)
}

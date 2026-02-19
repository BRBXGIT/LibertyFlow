package com.example.common.vm_helpers.models

import androidx.compose.runtime.Immutable

/**
 * Represents the state of a search input field.
 *
 * This class captures the current user input and whether a search operation
 * (like a network request or local filtering) is actively in progress.
 *
 * @property query The current text string entered by the user in the search field.
 * @property isSearching True if user inputting text in the ui search field.
 */
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
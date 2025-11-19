package com.example.home.screen

// Intent / Actions coming from the UI to the ViewModel
sealed interface HomeIntent {
    // Request a random anime (one-shot)
    object GetRandomAnime : HomeIntent

    // Update the search query text
    data class UpdateQuery(val query: String) : HomeIntent

    // Toggle search UI mode (enter/exit)
    object UpdateIsSearching : HomeIntent

    // Report paging loading state (used by PagingStatesContainer)
    data class UpdateIsLoading(val isLoading: Boolean) : HomeIntent

    // Report paging error state (used by PagingStatesContainer)
    data class UpdateIsError(val isError: Boolean) : HomeIntent
}

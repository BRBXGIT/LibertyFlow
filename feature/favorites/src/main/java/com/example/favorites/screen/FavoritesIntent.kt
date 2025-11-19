package com.example.favorites.screen

// Intents / actions from the UI to the ViewModel
sealed interface FavoritesIntent {

    // --- One-shot actions ---

    // Request authentication token
    object GetTokens : FavoritesIntent


    // --- Search UI ---

    // Update favorites search query
    data class UpdateQuery(val query: String) : FavoritesIntent

    // Toggle search UI mode (enter/exit)
    object UpdateIsSearching : FavoritesIntent


    // --- Auth UI / BottomSheet ---

    // Toggle auth bottom sheet visibility
    object UpdateIsAuthBSVisible : FavoritesIntent

    // Update email input
    data class UpdateEmail(val email: String) : FavoritesIntent

    // Update password input
    data class UpdatePassword(val password: String) : FavoritesIntent


    // --- Paging / UI status ---

    // Report loading state
    data class UpdateIsLoading(val isLoading: Boolean) : FavoritesIntent

    // Report error state
    data class UpdateIsError(val isError: Boolean) : FavoritesIntent
}
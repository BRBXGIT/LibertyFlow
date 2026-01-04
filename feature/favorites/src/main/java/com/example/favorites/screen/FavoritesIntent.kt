package com.example.favorites.screen

sealed interface FavoritesIntent {

    // Auth
    data object GetTokens : FavoritesIntent
    data object ToggleIsAuthBSVisible : FavoritesIntent
    data class UpdateEmail(val email: String) : FavoritesIntent
    data class UpdatePassword(val password: String) : FavoritesIntent

    // Search
    data object ToggleIsSearching : FavoritesIntent
    data class UpdateQuery(val query: String) : FavoritesIntent

    // UI flags
    data class SetIsLoading(val value: Boolean) : FavoritesIntent
    data class SetIsError(val value: Boolean) : FavoritesIntent
}

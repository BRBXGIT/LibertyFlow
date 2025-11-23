package com.example.favorites.screen

// Intents / actions from the UI to the ViewModel
sealed interface FavoritesIntent {

    object GetTokens: FavoritesIntent

    data class UpdateQuery(val query: String): FavoritesIntent
    data object ToggleIsSearching: FavoritesIntent

    data object ToggleIsAuthBSVisible: FavoritesIntent
    data class UpdateEmail(val email: String): FavoritesIntent
    data class UpdatePassword(val password: String): FavoritesIntent

    data class UpdateIsLoading(val isLoading: Boolean): FavoritesIntent
    data class UpdateIsError(val isError: Boolean): FavoritesIntent
}
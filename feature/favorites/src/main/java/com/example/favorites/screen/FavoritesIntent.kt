package com.example.favorites.screen

// Intents / actions from the UI to the ViewModel
sealed interface FavoritesIntent {

    object GetTokens: FavoritesIntent

    data object ToggleIsSearching: FavoritesIntent
    data object ToggleIsAuthBSVisible: FavoritesIntent

    data class UpdateQuery(val query: String): FavoritesIntent
    data class UpdateEmail(val email: String): FavoritesIntent
    data class UpdatePassword(val password: String): FavoritesIntent

    data class SetIsLoading(val value: Boolean): FavoritesIntent
    data class SetIsError(val value: Boolean): FavoritesIntent

    data class NavigateToAnimeDetails(val id: Int): FavoritesIntent
}
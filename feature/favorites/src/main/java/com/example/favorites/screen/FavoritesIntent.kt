package com.example.favorites.screen

/**
 * Represents all possible user or system intentions for the Favorites screen.
 * * As a [sealed interface], it ensures exhaustive handling within the ViewModel's
 * `sendIntent` function, providing a single source of truth for UI events.
 */
sealed interface FavoritesIntent {

    // Auth
    data object GetTokens: FavoritesIntent
    data object ToggleIsAuthBSVisible: FavoritesIntent
    data class UpdateLogin(val login: String): FavoritesIntent
    data class UpdatePassword(val password: String): FavoritesIntent


    // Search
    data object ToggleIsSearching: FavoritesIntent
    data class UpdateQuery(val query: String): FavoritesIntent


    // UI flags
    data class SetIsLoading(val value: Boolean): FavoritesIntent
    data class SetIsError(val value: Boolean): FavoritesIntent
}

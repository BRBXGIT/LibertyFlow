package com.example.favorites.screen

/**
 * Represents all possible user or system intentions for the Favorites screen.
 * * As a [sealed interface], it ensures exhaustive handling within the ViewModel's
 * `sendIntent` function, providing a single source of truth for UI events.
 */
sealed interface FavoritesIntent {

    // Auth
    data object GetTokens : FavoritesIntent
    data object ToggleIsAuthBSVisible : FavoritesIntent

    // Grouped form updates to avoid polluting the intent root
    data class UpdateAuthForm(val field: AuthField): FavoritesIntent {
        sealed interface AuthField {
            data class Email(val value: String): AuthField
            data class Password(val value: String): AuthField
        }
    }

    // Search
    data object ToggleIsSearching : FavoritesIntent
    data class UpdateQuery(val query: String) : FavoritesIntent

    // UI flags
    data class SetIsLoading(val value: Boolean) : FavoritesIntent
    data class SetIsError(val value: Boolean) : FavoritesIntent
}

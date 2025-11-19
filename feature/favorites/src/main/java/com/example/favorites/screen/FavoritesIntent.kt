package com.example.favorites.screen

sealed interface FavoritesIntent {
    data class UpdateIsLoading(val isLoading: Boolean): FavoritesIntent
    data class UpdateIsError(val isError: Boolean): FavoritesIntent

    data class UpdateQuery(val query: String): FavoritesIntent
    data object UpdateIsSearching: FavoritesIntent

    data object UpdateIsAuthBSVisible: FavoritesIntent
    data class UpdateEmail(val email: String): FavoritesIntent
    data class UpdatePassword(val password: String): FavoritesIntent

    data object GetTokens: FavoritesIntent
}
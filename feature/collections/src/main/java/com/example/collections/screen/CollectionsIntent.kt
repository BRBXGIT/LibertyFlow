package com.example.collections.screen

sealed interface CollectionsIntent {
    object GetTokens: CollectionsIntent

    data object ToggleIsSearching: CollectionsIntent
    data object ToggleIsAuthBSVisible: CollectionsIntent

    data class UpdateQuery(val query: String): CollectionsIntent
    data class UpdateEmail(val email: String): CollectionsIntent
    data class UpdatePassword(val password: String): CollectionsIntent

    data class SetIsLoading(val isLoading: Boolean): CollectionsIntent
    data class SetIsError(val isError: Boolean): CollectionsIntent
}
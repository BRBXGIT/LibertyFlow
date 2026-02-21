package com.example.anime_details.screen

import com.example.data.models.common.request.request_parameters.Collection

/**
 * Sealed interface representing all possible user actions or system events
 * within the Anime Details screen.
 *
 * These intents are dispatched from the UI to the [AnimeDetailsVM], where
 * they are processed to update the state or trigger side effects.
 */
sealed interface AnimeDetailsIntent {
    // --- Data Operations ---
    data class FetchAnime(val id: Int) : AnimeDetailsIntent
    data object GetTokens: AnimeDetailsIntent

    // --- Favorites Operations ---
    data object AddToFavorite: AnimeDetailsIntent
    data object RemoveFromFavorite: AnimeDetailsIntent

    // Collections operations
    data class ToggleCollection(val collection: Collection): AnimeDetailsIntent

    // --- UI Interactions ---
    data object ToggleIsDescriptionExpanded: AnimeDetailsIntent
    data object ToggleIsAuthBSVisible: AnimeDetailsIntent
    data object ToggleCollectionsBSVisible: AnimeDetailsIntent

    // Auth
    data class UpdateLogin(val login: String): AnimeDetailsIntent
    data class UpdatePassword(val password: String): AnimeDetailsIntent
}
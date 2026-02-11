package com.example.anime_details.screen

import com.example.data.models.common.request.request_parameters.Collection

sealed interface AnimeDetailsIntent {
    // --- Data Operations ---
    data class FetchAnime(val id: Int) : AnimeDetailsIntent
    data class ObserveWatchedEps(val id: Int) : AnimeDetailsIntent
    data class AddEpisodeToWatched(val episodeIndex: Int): AnimeDetailsIntent
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

    // Grouped form updates to avoid polluting the intent root
    data class UpdateAuthForm(val field: AuthField): AnimeDetailsIntent {
        sealed interface AuthField {
            data class Email(val value: String): AuthField
            data class Password(val value: String): AuthField
        }
    }
}
package com.example.anime_details.screen

sealed interface AnimeDetailsIntent {

    // Data
    data class FetchAnime(val id: Int): AnimeDetailsIntent
    data class ObserveWatchedEps(val id: Int): AnimeDetailsIntent
    data class AddEpisodeToWatched(val episodeIndex: Int): AnimeDetailsIntent
    data object GetTokens: AnimeDetailsIntent

    // Favorites
    data object AddToFavorite: AnimeDetailsIntent
    data object RemoveFromFavorite: AnimeDetailsIntent

    // Ui
    data object ToggleIsDescriptionExpanded: AnimeDetailsIntent
    data object ToggleIsAuthBsVisible: AnimeDetailsIntent
    data class UpdateEmail(val email: String): AnimeDetailsIntent
    data class UpdatePassword(val password: String): AnimeDetailsIntent
}
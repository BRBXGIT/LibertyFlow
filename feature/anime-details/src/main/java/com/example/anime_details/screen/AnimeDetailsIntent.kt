package com.example.anime_details.screen

sealed interface AnimeDetailsIntent {

    // Data
    data class FetchAnime(val id: Int): AnimeDetailsIntent
    data class ObserveWatchedEps(val id: Int): AnimeDetailsIntent
    data class AddEpisodeToWatched(val episodeIndex: Int): AnimeDetailsIntent

    // Ui
    data object ToggleIsDescriptionExpanded: AnimeDetailsIntent
}
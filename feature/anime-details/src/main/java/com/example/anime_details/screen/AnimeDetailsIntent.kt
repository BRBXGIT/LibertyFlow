package com.example.anime_details.screen

sealed interface AnimeDetailsIntent {

    // Data
    data class FetchAnime(val id: Int): AnimeDetailsIntent

    // Ui
    data object ToggleIsDescriptionExpanded: AnimeDetailsIntent
}
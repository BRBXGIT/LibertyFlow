package com.example.anime_details.screen

sealed interface AnimeDetailsIntent {
    data class FetchAnime(val id: Int): AnimeDetailsIntent
}
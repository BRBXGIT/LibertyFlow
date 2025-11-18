package com.example.home.screen

sealed interface HomeIntent {
    data object GetRandomAnime: HomeIntent

    data class UpdateQuery(val query: String): HomeIntent
    data object UpdateIsSearching: HomeIntent

    data class UpdateIsLoading(val isLoading: Boolean): HomeIntent
}
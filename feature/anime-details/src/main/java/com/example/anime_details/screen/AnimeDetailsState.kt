package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState
import com.example.data.models.releases.anime_details.UiAnimeDetails

@Immutable
data class AnimeDetailsState(
    // Auth
    val authState: AuthState = AuthState.LoggedOut,

    // Sets
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val watchedEps: List<Int> = emptyList(),
    val isFavoritesLoading: Boolean = false,
    val isFavoritesError: Boolean = false,

    // Anime data
    val anime: UiAnimeDetails? = null,

    // Toggles
    val isDescriptionExpanded: Boolean = false,

    // Updates
    val favoritesIds: List<Int> = emptyList()
) {
    // Sets
    fun setAuthState(value: AuthState) = copy(authState = value)
    fun setWatchedEps(value: List<Int>) = copy(watchedEps = value)

    // Toggles
    fun toggleIsDescriptionExpanded() = copy(isDescriptionExpanded = !isDescriptionExpanded)
}

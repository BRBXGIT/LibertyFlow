package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState
import com.example.data.models.releases.anime_details.UiAnimeDetails

@Immutable
data class AnimeDetailsState(
    val authState: AuthState = AuthState.LoggedOut,

    val isLoading: Boolean = false,
    val isError: Boolean = false,

    val anime: UiAnimeDetails? = null,
) {
    fun setAuthState(value: AuthState) = copy(authState = value)
}

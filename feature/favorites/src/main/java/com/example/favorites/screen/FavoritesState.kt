package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState

@Immutable
data class FavoritesState(
    val isLoggedIn: AuthState = AuthState.LoggedOut,

    val isLoading: Boolean = false,
    val isError: Boolean = false,

    val isAuthBSVisible: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordOrEmailIncorrect: Boolean = false,

    val query: String = "",
    val isSearching: Boolean = false
)
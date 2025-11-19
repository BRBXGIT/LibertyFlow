package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState

@Immutable
data class FavoritesState(

    // whether user is logged in
    val isLoggedIn: AuthState = AuthState.LoggedOut,

    // whether auth or favorites loading is in progress
    val isLoading: Boolean = false,

    // whether any general error occurred (network / server, except wrong credentials)
    val isError: Boolean = false,

    // whether auth bottom sheet is visible
    val isAuthBSVisible: Boolean = false,

    // email for login
    val email: String = "",

    // password for login
    val password: String = "",

    // incorrect email or password (shown only after failed login attempt)
    val isPasswordOrEmailIncorrect: Boolean = false,

    // search query for filtering favorites
    val query: String = "",

    // whether search mode is active
    val isSearching: Boolean = false
)
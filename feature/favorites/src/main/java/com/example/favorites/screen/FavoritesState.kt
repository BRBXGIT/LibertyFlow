package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState

@Immutable
data class FavoritesState(
    val authState: AuthState = AuthState.LoggedOut,

    val isLoading: Boolean = false,
    val isError: Boolean = false,

    val isAuthBSVisible: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordOrEmailIncorrect: Boolean = false,

    val query: String = "",
    val isSearching: Boolean = false
) {
    fun toggleAuthBS() = copy(isAuthBSVisible = !isAuthBSVisible)

    fun toggleIsSearching() = copy(isSearching = !isSearching)

    fun setLoading(value: Boolean) = copy(isLoading = value)

    fun setError(value: Boolean) = copy(isError = value)

    fun updateQuery(query: String) = copy(query = query)

    fun updateEmail(email: String) = copy(email = email)

    fun updatePassword(password: String) = copy(password = password)
}
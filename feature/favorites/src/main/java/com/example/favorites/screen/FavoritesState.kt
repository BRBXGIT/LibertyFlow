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

    /* --- UI toggles --- */

    fun toggleAuthBS() =
        copy(isAuthBSVisible = !isAuthBSVisible)

    fun toggleSearching() =
        copy(isSearching = !isSearching)

    /* --- Flags --- */

    fun withLoading(value: Boolean) =
        copy(isLoading = value)

    fun withError(value: Boolean) =
        copy(isError = value)

    fun withAuthState(state: AuthState) =
        copy(authState = state)

    /* --- Search --- */

    fun updateQuery(query: String) =
        copy(query = query)

    /* --- Auth input --- */

    fun updateAuthInput(
        email: String = this.email,
        password: String = this.password
    ) = copy(
        email = email,
        password = password
    )
}
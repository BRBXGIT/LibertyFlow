package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.data.models.auth.AuthState

@Immutable
data class FavoritesState(
    val authState: AuthState = AuthState.LoggedOut,

    val isLoading: Boolean = false,
    val isError: Boolean = false,

    val isAuthBSVisible: Boolean = false,
    val authForm: AuthFormState = AuthFormState(),


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

    // Using `copy` on nested objects keeps the main `copy` clean
    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): FavoritesState {
        return copy(authForm = transformer(authForm))
    }
}
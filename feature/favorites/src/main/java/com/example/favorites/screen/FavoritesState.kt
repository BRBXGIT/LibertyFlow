package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.common.ui_helpers.search.SearchForm
import com.example.data.models.auth.AuthState

@Immutable
data class FavoritesState(
    val authState: AuthState = AuthState.LoggedOut,

    val loadingState: LoadingState = LoadingState(),

    val authForm: AuthFormState = AuthFormState(),

    val searchForm: SearchForm = SearchForm()
) {
    // Auth
    fun withAuthState(state: AuthState) =
        copy(authState = state)
    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): FavoritesState {
        return copy(authForm = transformer(authForm))
    }
}
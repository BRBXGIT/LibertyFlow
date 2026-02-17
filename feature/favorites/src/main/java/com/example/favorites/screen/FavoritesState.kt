package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.common.ui_helpers.search.SearchForm
import com.example.data.models.auth.AuthState

/**
 * Represents the complete UI state for the Favorites screen.
 * * This data class is marked as [@Immutable] to optimize Compose recomposition.
 * It encapsulates authentication status, global loading/error states, and
 * specific form states for both login and searching.
 *
 * @property authState The current user authentication status (e.g., LoggedIn, LoggedOut).
 * @property loadingState Handles the visibility of progress indicators and error messages.
 * @property authForm State specifically for the authentication BottomSheet and input fields.
 * @property searchForm State managing the search bar visibility and query text.
 */
@Immutable
data class FavoritesState(
    val authState: AuthState = AuthState.LoggedOut,

    val loadingState: LoadingState = LoadingState(),

    val authForm: AuthFormState = AuthFormState(),

    val searchForm: SearchForm = SearchForm()
) {
    // Toggles
    fun toggleAuthBS() = copy(authForm = authForm.copy(isAuthBSVisible = !authForm.isAuthBSVisible))

    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): FavoritesState {
        return copy(authForm = transformer(authForm))
    }
}
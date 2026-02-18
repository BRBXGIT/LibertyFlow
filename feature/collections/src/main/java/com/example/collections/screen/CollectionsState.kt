package com.example.collections.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.common.ui_helpers.search.SearchForm
import com.example.data.models.auth.AuthState
import com.example.data.models.common.request.request_parameters.Collection

/**
 * Represents the complete UI state for the Collections screen.
 * * This data class is marked as [Immutable] to allow Compose to optimize recompositions.
 * It encapsulates authentication status, error states, and form-specific sub-states.
 *
 * @property authState The current global authentication status (e.g., LoggedIn, LoggedOut).
 * @property isError A general flag indicating if the screen is currently in an error state.
 * @property authForm The sub-state managing the authentication BottomSheet and credentials.
 * @property searchForm The sub-state managing search queries and active search status.
 * @property selectedCollection The currently active collection tab (e.g., 'WATCHING', 'PLANNED').
 */
@Immutable
data class CollectionsState(
    val authState: AuthState = AuthState.LoggedOut,

    val isError: Boolean = false,

    val authForm: AuthFormState = AuthFormState(),

    val searchForm: SearchForm = SearchForm(),

    val selectedCollection: Collection = Collection.WATCHING
) {
    fun setError(value: Boolean) = copy(isError = value)

    fun setAuthState(value: AuthState) = copy(authState = value)

    // Using `copy` on nested objects keeps the main `copy` clean
    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): CollectionsState {
        return copy(authForm = transformer(authForm))
    }
}

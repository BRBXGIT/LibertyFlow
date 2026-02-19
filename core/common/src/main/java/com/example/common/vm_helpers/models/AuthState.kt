package com.example.common.vm_helpers.models

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.UserAuthState

/**
 * Represents the UI state for the authentication form.
 *
 * This class is marked as [@Immutable] to allow Compose to optimize recompositions,
 * ensuring that the UI only updates when the state properties actually change.
 *
 * @property userAuthState The current user auth state, logged in or logged out.
 * @property isAuthBSVisible Controls the visibility of the authentication bottom sheet.
 * @property login The current text input for the user's login or email.
 * @property password The current text input for the user's password.
 * @property isError Indicates if there is a validation error or a failed login attempt
 * that should be reflected in the UI (e.g., showing red outlines or error messages).
 */
@Immutable
data class AuthState(
    val userAuthState: UserAuthState = UserAuthState.LoggedOut,
    val isAuthBSVisible: Boolean = false,
    val login: String = "",
    val password: String = "",
    val isError: Boolean = false
) {
    fun toggleIsAuthBSVisible() = copy(isAuthBSVisible = !isAuthBSVisible)
}
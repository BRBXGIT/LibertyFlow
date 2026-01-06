package com.example.collections.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.data.models.auth.AuthState
import com.example.data.models.common.request.request_parameters.Collection

@Immutable
data class CollectionsState(
    val authState: AuthState = AuthState.LoggedOut,

    val isError: Boolean = false,

    val isAuthBSVisible: Boolean = false,
    val authForm: AuthFormState = AuthFormState(),

    val query: String = "",
    val isSearching: Boolean = false,

    val selectedCollection: Collection = Collection.WATCHING
) {
    fun toggleAuthBS() = copy(isAuthBSVisible = !isAuthBSVisible)

    fun toggleIsSearching() = copy(isSearching = !isSearching)

    fun setError(value: Boolean) = copy(isError = value)

    fun setAuthState(value: AuthState) = copy(authState = value)

    fun setCollection(collection: Collection) = copy(selectedCollection = collection)

    fun updateQuery(query: String) = copy(query = query)

    // Using `copy` on nested objects keeps the main `copy` clean
    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): CollectionsState {
        return copy(authForm = transformer(authForm))
    }
}

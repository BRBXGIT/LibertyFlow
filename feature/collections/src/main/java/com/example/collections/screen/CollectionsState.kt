package com.example.collections.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState
import com.example.data.models.common.request.request_parameters.Collection

@Immutable
data class CollectionsState(
    val authState: AuthState = AuthState.LoggedOut,

    val isError: Boolean = false,
    val isLoading: Boolean = false,

    val isAuthBSVisible: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordOrEmailIncorrect: Boolean = false,

    val query: String = "",
    val isSearching: Boolean = false,

    val selectedCollection: Collection = Collection.WATCHING
) {
    fun toggleAuthBS() = copy(isAuthBSVisible = !isAuthBSVisible)

    fun toggleIsSearching() = copy(isSearching = !isSearching)

    fun setLoading(value: Boolean) = copy(isLoading = value)

    fun setError(value: Boolean) = copy(isError = value)

    fun setCollection(collection: Collection) = copy(selectedCollection = collection)

    fun updateQuery(query: String) = copy(query = query)

    fun updateEmail(email: String) = copy(email = email)

    fun updatePassword(password: String) = copy(password = password)
}

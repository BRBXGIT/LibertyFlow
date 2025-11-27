package com.example.collections.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState
import com.example.data.models.common.request.request_parameters.CollectionType

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

    val selectedCollection: CollectionType = CollectionType.WATCHING
)

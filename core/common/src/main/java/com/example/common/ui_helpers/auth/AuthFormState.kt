package com.example.common.ui_helpers.auth

import androidx.compose.runtime.Immutable

@Immutable
data class AuthFormState(
    val isAuthBSVisible: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isError: Boolean = false
) {
    fun toggleIsAuthBSVisible() = copy(isAuthBSVisible = !isAuthBSVisible)
}
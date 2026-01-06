package com.example.common.ui_helpers.auth

import androidx.compose.runtime.Immutable

@Immutable
data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val isError: Boolean = false
)
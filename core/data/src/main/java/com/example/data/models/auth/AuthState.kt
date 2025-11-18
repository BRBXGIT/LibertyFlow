package com.example.data.models.auth

sealed interface AuthState {
    data object LoggedIn: AuthState
    data object LoggedOut: AuthState
}
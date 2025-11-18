package com.example.home.screen

sealed interface HomeEffect {
    data class ShowRetrySnackbar(val message: String, val action: () -> Unit): HomeEffect
}
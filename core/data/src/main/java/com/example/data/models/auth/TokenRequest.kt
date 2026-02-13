package com.example.data.models.auth

/**
 * Ui auth model
 *
 * @property login The user's unique identifier, such as a username or email address.
 * @property password The plain-text password provided by the user.
 * Note: Ensure this is transmitted over HTTPS to maintain security.
 */
data class TokenRequest(
    val login: String,
    val password: String
)
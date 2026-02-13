package com.example.data.models.auth

/**
 * A wrapper for the authentication token.
 * @property token The raw string value of the access token.
 * Can be null if the token has expired or was never issued.
 */
data class Token(val token: String?)
package com.example.network.auth.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a user's login credentials.
 *
 * @property login The user's unique identifier (e.g., email or username).
 * @property password The user's plain-text password.
 */
data class SessionTokenRequestDto(
    @field:SerializedName(FIELD_LOGIN)
    val login: String,

    @field:SerializedName(FIELD_PASSWORD)
    val password: String
) {
    companion object Fields {
        private const val FIELD_LOGIN = "login"
        private const val FIELD_PASSWORD = "password"
    }
}

package com.example.network.auth.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the server's response to an authentication request.
 *
 * @property token The unique session string generated upon successful login.
 * May be null if authentication fails.
 * @property error A descriptive message or code explaining why the authentication failed.
 */
data class SessionTokenResponseDto(
    @field:SerializedName(FIELD_TOKEN)
    val token: String?,

    @field:SerializedName(FIELD_ERROR)
    val error: String
) {
    /**
     * Constants for JSON field names to avoid hardcoded strings.
     */
    companion object Fields {
        private const val FIELD_TOKEN = "token"
        private const val FIELD_ERROR = "error"
    }
}

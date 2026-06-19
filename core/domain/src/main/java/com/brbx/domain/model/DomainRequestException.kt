package com.brbx.domain.model

enum class DomainRequestException {
    // --- Http Errors (4xx, 5xx) ---
    CONFLICT,
    TOO_MANY_REQUESTS,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    INCORRECT_CREDENTIALS,
    UNAUTHORIZED,
    NO_EMAIL_OR_PASSWORD,
    // --- Local / Connectivity Errors ---
    REQUEST_TIMEOUT,
    INTERNET,
    SERIALIZATION,
    // --- Fallback ---
    UNKNOWN,
}
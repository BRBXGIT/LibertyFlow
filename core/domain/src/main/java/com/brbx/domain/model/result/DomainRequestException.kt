package com.brbx.domain.model.result

enum class DomainRequestException {
    // --- Http Errors (4xx, 5xx) ---
    Conflict,
    TooManyRequests,
    PayloadTooLarge,
    ServerError,
    IncorrectCredentials,
    Unauthorized,
    NoEmailOrPassword,
    // --- Local / Connectivity Errors ---
    RequestTimeout,
    Internet,
    Serialization,
    // --- Fallback ---
    Unknown,
}
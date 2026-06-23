package com.brbx.domain.network.model.result

enum class RequestException {
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
    // --- Fallback ---
    Unknown,
}
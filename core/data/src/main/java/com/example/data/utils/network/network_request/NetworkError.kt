package com.example.data.utils.network.network_request

interface NetworkError

/**
 * An enumeration of specific error states that can occur during a network operation.
 * * This enum implements [NetworkError], allowing it to be used within the
 * [NetworkResult] sealed hierarchy. It categorizes failures into three main groups:
 * 1. **HTTP Errors:** Responses from the server with non-2xx status codes.
 * 2. **Local Errors:** Issues originating from the device or network conditions.
 * 3. **Fallback:** A catch-all [UNKNOWN] state for unhandled exceptions.
 */
enum class NetworkErrors: NetworkError {
    // --- Http Errors (4xx, 5xx) ---
    CONFLICT,
    TOO_MANY_REQUESTS,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    INCORRECT_EMAIL_OR_PASSWORD,
    UNAUTHORIZED,
    NO_EMAIL_OR_PASSWORD,
    // --- Local / Connectivity Errors ---
    REQUEST_TIMEOUT,
    INTERNET,
    SERIALIZATION,
    // --- Fallback ---
    UNKNOWN,
}
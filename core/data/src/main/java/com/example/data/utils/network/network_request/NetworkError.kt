package com.example.data.utils.network.network_request

interface NetworkError

enum class NetworkErrors: NetworkError {
    // Http errors
    CONFLICT,
    TOO_MANY_REQUESTS,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    INCORRECT_EMAIL_OR_PASSWORD,
    UNAUTHORIZED,
    NO_EMAIL_OR_PASSWORD,
    // Local errors
    REQUEST_TIMEOUT,
    INTERNET,
    SERIALIZATION,
    // Unknown
    UNKNOWN,
}
package com.example.data.utils.network.network_request

/**
 * A sealed hierarchy representing the result of a network operation.
 * * Use this to encapsulate data in a type-safe manner, distinguishing between
 * successful outcomes and various error states.
 *
 * @param T The type of data expected in a successful response.
 */
sealed interface NetworkResult<out T> {

    /**
     * Represents a successful network call.
     * @property data The encapsulated result of type [T].
     */
    data class Success<out T>(val data: T) : NetworkResult<T>

    /**
     * Represents a failed network call.
     * @property error A specific [NetworkError] type (e.g., Timeout, Unauthorized).
     * @property messageRes A string resource ID for a user-facing error message.
     */
    data class Error(val error: NetworkError, val messageRes: Int) : NetworkResult<Nothing>
}

/**
 * Executes the given [block] if the result is an instance of [NetworkResult.Success].
 * * This is useful for chaining operations or triggering UI updates after a successful fetch.
 * @param block A lambda receiving the successful data of type [T].
 * @return The original [NetworkResult] for further chaining.
 */
inline fun <T> NetworkResult<T>.onSuccess(block: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) block(data)
    return this
}

/**
 * Executes the given [block] if the result is an instance of [NetworkResult.Error].
 * * Use this to handle logging, showing snackbars, or analytics tracking when a call fails.
 * @param block A lambda receiving the [NetworkError] and the message resource ID.
 * @return The original [NetworkResult] for further chaining.
 */
inline fun <T> NetworkResult<T>.onError(block: (NetworkError, Int) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) block(error, messageRes)
    return this
}
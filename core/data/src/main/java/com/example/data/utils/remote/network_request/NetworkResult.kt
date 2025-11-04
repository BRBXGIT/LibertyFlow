package com.example.data.utils.remote.network_request

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T): NetworkResult<T>
    data class Error(val error: NetworkError, val message: String): NetworkResult<Nothing>
}

inline fun <T> NetworkResult<T>.onSuccess(block: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) block(data)
    return this
}

inline fun <T> NetworkResult<T>.onError(block: (NetworkError, String) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) block(error, message)
    return this
}
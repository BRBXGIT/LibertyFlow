package com.example.data.utils.network.network_request

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T): NetworkResult<T>
    data class Error(val error: NetworkError, val messageRes: Int): NetworkResult<Nothing>
}

inline fun <T> NetworkResult<T>.onSuccess(block: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) block(data)
    return this
}

inline fun <T> NetworkResult<T>.onError(block: (NetworkError, Int) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) block(error, messageRes)
    return this
}
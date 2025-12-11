package com.example.data.utils.remote.network_request

import android.util.Log
import com.example.data.R
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal object NetworkRequest {

    suspend fun <T, R> safeApiCall(
        call: suspend () -> Response<T>,
        map: (T) -> R
    ): NetworkResult<R> {
        return when (val result = exec(call)) {
            is NetworkResult.Success -> NetworkResult.Success(map(result.data))
            is NetworkResult.Error -> result
        }
    }

    private suspend fun <T> exec(
        call: suspend () -> Response<T>
    ): NetworkResult<T> {
        return try {
            val response = call()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    errorResult(NetworkErrors.SERIALIZATION)
                }
            } else {
                errorResult(getErrorByCode(response.code()))
            }
        } catch (e: Exception) {
            errorResult(getErrorByException(e))
        }
    }

    private fun errorResult(error: NetworkError): NetworkResult.Error {
        val message = getMessageByError(error)
        return NetworkResult.Error(error, message)
    }
}

private fun getErrorByCode(statusCode: Int): NetworkError {
    return when (statusCode) {
        401 -> NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD
        422 -> NetworkErrors.NO_EMAIL_OR_PASSWORD
        408 -> NetworkErrors.REQUEST_TIMEOUT
        409 -> NetworkErrors.CONFLICT
        413 -> NetworkErrors.PAYLOAD_TOO_LARGE
        429 -> NetworkErrors.TOO_MANY_REQUESTS
        in 500..599 -> NetworkErrors.SERVER_ERROR
        else -> NetworkErrors.UNKNOWN
    }
}

private fun getMessageByError(error: NetworkError): String {
    return when (error) {
        NetworkErrors.REQUEST_TIMEOUT -> R.string.request_timeout_message.toString()
        NetworkErrors.CONFLICT -> R.string.conflict_message.toString()
        NetworkErrors.PAYLOAD_TOO_LARGE -> R.string.payload_too_large_message.toString()
        NetworkErrors.TOO_MANY_REQUESTS -> R.string.too_many_requests_message.toString()
        NetworkErrors.SERVER_ERROR -> R.string.server_error_message.toString()
        NetworkErrors.INTERNET -> R.string.internet_message.toString()
        NetworkErrors.SERIALIZATION -> R.string.serialization_message.toString()
        NetworkErrors.NO_EMAIL_OR_PASSWORD -> R.string.no_email_or_password_message.toString()
        NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD -> R.string.incorrect_email_or_password_message.toString()
        else -> R.string.unknown_message.toString()
    }
}

private fun getErrorByException(e: Exception): NetworkError {
    return when (e) {
        is UnknownHostException -> NetworkErrors.INTERNET
        is SocketException -> NetworkErrors.INTERNET
        is SocketTimeoutException -> NetworkErrors.REQUEST_TIMEOUT
        else -> NetworkErrors.UNKNOWN
    }
}
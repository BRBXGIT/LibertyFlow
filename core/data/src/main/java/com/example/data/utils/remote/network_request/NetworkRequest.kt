package com.example.data.utils.remote.network_request

import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkRequest {
    suspend fun <T> exec(
        call: suspend () -> Response<T>
    ): NetworkResult<T> {
        return try {
            val response = call()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(data = body)
                } else {
                    val error = NetworkErrors.SERIALIZATION
                    val message = getMessageByError(error)
                    NetworkResult.Error(error,message)
                }
            } else {
                val error = getErrorByCode(response.code())
                val message = getMessageByError(error)
                NetworkResult.Error(error, message)
            }
        } catch (e: Exception) {
            val error = getErrorByException(e)
            val message = getMessageByError(error)
            NetworkResult.Error(error, message)
        }
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
        NetworkErrors.REQUEST_TIMEOUT -> "Timeout please retry"
        NetworkErrors.CONFLICT -> "Something conflict"
        NetworkErrors.PAYLOAD_TOO_LARGE -> "The server load is too large"
        NetworkErrors.TOO_MANY_REQUESTS -> "Too many requests, give soundcloud a little rest :)"
        NetworkErrors.SERVER_ERROR -> "Server error"
        NetworkErrors.INTERNET -> "Please connect to network"
        NetworkErrors.SERIALIZATION -> "Serialization problem"
        NetworkErrors.NO_EMAIL_OR_PASSWORD -> "No email or password"
        NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD -> "Incorrect email or password"
        else -> "Unknown error"
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
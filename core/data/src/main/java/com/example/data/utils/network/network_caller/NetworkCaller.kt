package com.example.data.utils.network.network_caller

import com.example.data.R
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for executing network requests safely within a [try-catch] block.
 *
 * This class handles the conversion of standard Retrofit [Response] objects and
 * platform exceptions into a unified [NetworkResult].
 */
@Singleton
class NetworkCaller @Inject constructor() {

    /**
     * Executes a network call and maps the successful response body [T] to a domain model [R].
     * @param T The type of the raw API response body.
     * @param R The desired output type (usually a Domain model).
     * @param call A suspend lambda that executes the network request.
     * @param map A transformation function to convert the API model [T] into the domain model [R].
     * @return A [NetworkResult] containing either the mapped data or a categorized error.
     */
    suspend fun <T, R> safeApiCall(
        call: suspend () -> Response<T>,
        map: (T) -> R
    ): NetworkResult<R> {
        return when (val result = exec(call)) {
            is NetworkResult.Success -> NetworkResult.Success(map(result.data))
            is NetworkResult.Error -> result
        }
    }

    /**
     * Internal executor that performs the actual network call and handles
     * HTTP status codes and exceptions.
     * @param call The suspend network operation.
     * @return [NetworkResult.Success] if the body is non-null and the call is successful;
     * otherwise returns [NetworkResult.Error].
     */
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

    /**
     * Helper to wrap a [NetworkError] into a [NetworkResult.Error] by
     * resolving its corresponding string resource.
     */
    private fun errorResult(error: NetworkError): NetworkResult.Error {
        val message = getMessageByError(error)
        return NetworkResult.Error(error, message)
    }

    /**
     * Maps HTTP status codes to defined [NetworkError] types.
     * @param statusCode The raw HTTP integer code (e.g., 401, 500).
     * @return The corresponding [NetworkError].
     */
    private fun getErrorByCode(statusCode: Int): NetworkError {
        return when (statusCode) {
            401 -> NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD
            403 -> NetworkErrors.UNAUTHORIZED
            422 -> NetworkErrors.NO_EMAIL_OR_PASSWORD
            408 -> NetworkErrors.REQUEST_TIMEOUT
            409 -> NetworkErrors.CONFLICT
            413 -> NetworkErrors.PAYLOAD_TOO_LARGE
            429 -> NetworkErrors.TOO_MANY_REQUESTS
            in 500..599 -> NetworkErrors.SERVER_ERROR
            else -> NetworkErrors.UNKNOWN
        }
    }

    /**
     * Resolves a user-friendly string resource ID for a given [NetworkError].
     * @param error The categorized error.
     * @return A resource ID (`R.string...`) suitable for display in the UI.
     */
    private fun getMessageByError(error: NetworkError): Int {
        return when (error) {
            NetworkErrors.UNAUTHORIZED -> R.string.unauthoried_message
            NetworkErrors.REQUEST_TIMEOUT -> R.string.request_timeout_message
            NetworkErrors.CONFLICT -> R.string.conflict_message
            NetworkErrors.PAYLOAD_TOO_LARGE -> R.string.payload_too_large_message
            NetworkErrors.TOO_MANY_REQUESTS -> R.string.too_many_requests_message
            NetworkErrors.SERVER_ERROR -> R.string.server_error_message
            NetworkErrors.INTERNET -> R.string.internet_message
            NetworkErrors.SERIALIZATION -> R.string.serialization_message
            NetworkErrors.NO_EMAIL_OR_PASSWORD -> R.string.no_email_or_password_message
            NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD -> R.string.incorrect_email_or_password_message
            else -> R.string.unknown_message
        }
    }

    /**
     * Maps common networking exceptions to [NetworkError] types.
     * @param e The caught [Exception].
     * @return A [NetworkErrors.INTERNET] error for connectivity issues or [NetworkErrors.UNKNOWN].
     */
    private fun getErrorByException(e: Exception): NetworkError {
        return when (e) {
            is UnknownHostException -> NetworkErrors.INTERNET
            is SocketException -> NetworkErrors.INTERNET
            is SocketTimeoutException -> NetworkErrors.REQUEST_TIMEOUT
            else -> NetworkErrors.UNKNOWN
        }
    }
}
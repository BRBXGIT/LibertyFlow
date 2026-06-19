package com.brbx.network.base.api

import com.brbx.network.base.model.result.RequestException
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class ApiCallExecutorImpl : ApiCallExecutor {

    override suspend fun <T> execute(call: suspend () -> T): RequestResult<T> {
        return try {
            val data = call()
            RequestResult.Success(data)
        } catch (e: ResponseException) {
            RequestResult.Error(exception = e.response.status.value.toRequestException())
        } catch (e: Exception) {
            RequestResult.Error(exception = e.toRequestException())
        }
    }

    private fun Int.toRequestException(): RequestException = when (this) {
        401 -> RequestException.INCORRECT_CREDENTIALS
        403 -> RequestException.UNAUTHORIZED
        422 -> RequestException.NO_EMAIL_OR_PASSWORD
        408 -> RequestException.REQUEST_TIMEOUT
        409 -> RequestException.CONFLICT
        413 -> RequestException.PAYLOAD_TOO_LARGE
        429 -> RequestException.TOO_MANY_REQUESTS
        in 500..599 -> RequestException.SERVER_ERROR
        else -> RequestException.UNKNOWN
    }

    private fun Exception.toRequestException(): RequestException = when (this) {
        is UnknownHostException -> RequestException.INTERNET
        is SocketException -> RequestException.INTERNET
        is SocketTimeoutException -> RequestException.REQUEST_TIMEOUT
        is SerializationException -> RequestException.SERIALIZATION
        else -> RequestException.UNKNOWN
    }
}
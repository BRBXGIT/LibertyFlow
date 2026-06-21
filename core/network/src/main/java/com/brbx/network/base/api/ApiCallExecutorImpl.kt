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
        401 -> RequestException.IncorrectCredentials
        403 -> RequestException.Unauthorized
        422 -> RequestException.NoEmailOrPassword
        408 -> RequestException.RequestTimeout
        409 -> RequestException.Conflict
        413 -> RequestException.PayloadTooLarge
        429 -> RequestException.TooManyRequests
        in 500..599 -> RequestException.ServerError
        else -> RequestException.Unknown
    }

    private fun Exception.toRequestException(): RequestException = when (this) {
        is UnknownHostException -> RequestException.Internet
        is SocketException -> RequestException.Internet
        is SocketTimeoutException -> RequestException.RequestTimeout
        is SerializationException -> RequestException.Serialization
        else -> RequestException.Unknown
    }
}
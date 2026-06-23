package com.brbx.data.network.api.executor

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.model.result.RequestException
import com.brbx.network.base.model.result.RequestResult
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class ApiCallExecutorImpl : ApiCallExecutor {

    override suspend fun <T, R> execute(
        mapper: (T) -> R,
        call: suspend () -> RequestResult<T>,
    ): DomainRequestResult<R> =
        try {
            val data = call()
            data.toDomain(transform = mapper)
        } catch (e: Exception) {
            DomainRequestResult.Error(exception = e.toRequestException())
        }

    private fun <T, R> RequestResult<T>.toDomain(
        transform: (T) -> R,
    ): DomainRequestResult<R> =
        when (this) {
            is RequestResult.Success -> DomainRequestResult.Success(data = transform(this.data))
            is RequestResult.Error -> DomainRequestResult.Error(
                exception = this.code.toRequestException()
            )
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
        else -> RequestException.Unknown
    }
}
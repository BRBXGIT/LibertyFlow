package com.brbx.data.common.map

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.result.DomainRequestException
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.network.base.model.common.Genre
import com.brbx.network.base.model.result.RequestException
import com.brbx.network.base.model.result.RequestResult

private fun RequestException.toDomain(): DomainRequestException =
    when (this) {
        RequestException.CONFLICT -> DomainRequestException.CONFLICT
        RequestException.TOO_MANY_REQUESTS -> DomainRequestException.TOO_MANY_REQUESTS
        RequestException.PAYLOAD_TOO_LARGE -> DomainRequestException.PAYLOAD_TOO_LARGE
        RequestException.SERVER_ERROR -> DomainRequestException.SERVER_ERROR
        RequestException.INCORRECT_CREDENTIALS -> DomainRequestException.INCORRECT_CREDENTIALS
        RequestException.UNAUTHORIZED -> DomainRequestException.UNAUTHORIZED
        RequestException.NO_EMAIL_OR_PASSWORD -> DomainRequestException.NO_EMAIL_OR_PASSWORD
        RequestException.REQUEST_TIMEOUT -> DomainRequestException.REQUEST_TIMEOUT
        RequestException.INTERNET -> DomainRequestException.INTERNET
        RequestException.SERIALIZATION -> DomainRequestException.SERIALIZATION
        RequestException.UNKNOWN -> DomainRequestException.UNKNOWN
    }

internal inline fun <T, R> RequestResult<T>.toDomain(
    transform: (T) -> R
): DomainRequestResult<R> =
    when (this) {
        is RequestResult.Success -> DomainRequestResult.Success(data = transform(this.data))
        is RequestResult.Error -> DomainRequestResult.Error(exception = this.exception.toDomain())
    }

internal fun Genre.toDomain(): DomainGenre =
    DomainGenre(
        id = this.id,
        name = this.name,
    )
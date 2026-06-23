package com.brbx.domain.network.model.result

sealed interface DomainRequestResult<out T> {

    @JvmInline
    value class Success<T>(val data: T) : DomainRequestResult<T>

    @JvmInline
    value class Error(val exception: RequestException) : DomainRequestResult<Nothing>
}
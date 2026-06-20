package com.brbx.domain.model.result

interface DomainRequestResult<out T> {

    @JvmInline
    value class Success<T>(val data: T) : DomainRequestResult<T>

    @JvmInline
    value class Error(val exception: DomainRequestException) : DomainRequestResult<Nothing>
}
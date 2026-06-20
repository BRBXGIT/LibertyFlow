package com.brbx.domain.model.result

interface DomainRequestResult<out T> {

    data class Success<T>(val data: T) : DomainRequestResult<T>

    data class Error(
        val exception: DomainRequestException,
        val message: String,
    ) : DomainRequestResult<Nothing>
}
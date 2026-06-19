package com.brbx.network.base.model.result

sealed interface RequestResult<out T> {

    @JvmInline
    value class Success<out T>(val data: T) : RequestResult<T>

    @JvmInline
    value class Error(val exception: RequestException) : RequestResult<Nothing>
}
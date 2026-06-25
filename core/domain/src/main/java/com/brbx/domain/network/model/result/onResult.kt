package com.brbx.domain.network.model.result

inline infix fun <T> DomainRequestResult<T>.onSuccess(
    action: (T) -> Unit
): DomainRequestResult<T> {
    if (this is DomainRequestResult.Success) {
        action(this.data)
    }
    return this
}

inline infix fun <T> DomainRequestResult<T>.onError(
    action: (RequestException) -> Unit
): DomainRequestResult<T> {
    if (this is DomainRequestResult.Error) {
        action(this.exception)
    }
    return this
}
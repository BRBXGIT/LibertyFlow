package com.brbx.data.network.api.executor

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.network.base.model.result.RequestResult

internal interface ApiCallExecutor {

    suspend fun <T, R> execute(
        mapper: (T) -> R,
        call: suspend () -> RequestResult<T>,
    ): DomainRequestResult<R>
}
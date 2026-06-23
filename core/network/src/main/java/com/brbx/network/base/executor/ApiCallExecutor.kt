package com.brbx.network.base.executor

import com.brbx.network.base.model.result.RequestResult

internal interface ApiCallExecutor {

    suspend fun <T> execute(call: suspend () -> T): RequestResult<T>
}
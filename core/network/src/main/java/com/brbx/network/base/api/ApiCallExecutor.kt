package com.brbx.network.base.api

import com.brbx.network.base.model.result.RequestResult

interface ApiCallExecutor {

    suspend fun <T> execute(call: suspend () -> T): RequestResult<T>
}
package com.brbx.network.base.executor

import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.plugins.ResponseException

internal class ApiCallExecutorImpl : ApiCallExecutor {

    override suspend fun <T> execute(call: suspend () -> T): RequestResult<T> {
        return try {
            val data = call()
            RequestResult.Success(data)
        } catch (e: ResponseException) {
            RequestResult.Error(code = e.response.status.value)
        }
    }
}
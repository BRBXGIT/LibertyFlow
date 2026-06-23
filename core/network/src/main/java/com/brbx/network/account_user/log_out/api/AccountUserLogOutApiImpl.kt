package com.brbx.network.account_user.log_out.api

import com.brbx.network.base.executor.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.request.post

internal class AccountUserLogOutApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserLogOutApi {

    override suspend fun logOut(): RequestResult<Unit> =
        apiCallExecutor.execute {
            apiClientProvider.client.post(urlString = LogOutEndPoint)
        }

    private companion object {

        const val LogOutEndPoint = "accounts/users/auth/logout"
    }
}
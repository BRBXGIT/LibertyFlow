package com.brbx.network.account_user.auth.api

import com.brbx.network.account_user.auth.model.AuthForm
import com.brbx.network.account_user.auth.model.Token
import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AccountUserAuthApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AccountUserAuthApi {

    override suspend fun auth(authForm: AuthForm): RequestResult<Token> =
        apiCallExecutor.execute {
            apiClientProvider.client.post(
                urlString = AccountUserAuthApiDefaults.AuthEndPoint,
            ) {
                setBody(authForm)
            }.body()
        }
}
package com.brbx.network.account_user.auth.api

import com.brbx.network.account_user.auth.model.AuthForm
import com.brbx.network.account_user.auth.model.Token
import com.brbx.network.base.model.result.RequestResult

interface AccountUserAuthApi {

    suspend fun auth(authForm: AuthForm): RequestResult<Token>
}
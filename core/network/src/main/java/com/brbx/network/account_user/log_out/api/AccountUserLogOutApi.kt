package com.brbx.network.account_user.log_out.api

import com.brbx.network.base.model.result.RequestResult

interface AccountUserLogOutApi {

    suspend fun logOut(): RequestResult<Unit>
}
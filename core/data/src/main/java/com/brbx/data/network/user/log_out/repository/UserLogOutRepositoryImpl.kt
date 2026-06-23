package com.brbx.data.network.user.log_out.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.log_out.repository.UserLogOutRepository
import com.brbx.network.account_user.log_out.api.AccountUserLogOutApi

internal class UserLogOutRepositoryImpl(
    private val api: AccountUserLogOutApi,
    private val executor: ApiCallExecutor,
) : UserLogOutRepository {

    override suspend fun logOut(): DomainRequestResult<Unit> =
        executor.execute(mapper = {}) {
            api.logOut()
        }
}
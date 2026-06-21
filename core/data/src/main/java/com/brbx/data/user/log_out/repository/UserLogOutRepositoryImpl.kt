package com.brbx.data.user.log_out.repository

import com.brbx.data.common.map.toDomain
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.log_out.repository.UserLogOutRepository
import com.brbx.network.account_user.log_out.api.AccountUserLogOutApi

internal class UserLogOutRepositoryImpl(
    private val api: AccountUserLogOutApi,
) : UserLogOutRepository {

    override suspend fun logOut(): DomainRequestResult<Unit> =
        api.logOut().toDomain {}
}
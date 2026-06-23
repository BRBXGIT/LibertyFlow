package com.brbx.domain.network.user.log_out.repository

import com.brbx.domain.network.model.result.DomainRequestResult

interface UserLogOutRepository {

    suspend fun logOut(): DomainRequestResult<Unit>
}
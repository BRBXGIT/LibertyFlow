package com.brbx.domain.user.log_out.repository

import com.brbx.domain.model.result.DomainRequestResult

interface UserLogOutRepository {

    suspend fun logOut(): DomainRequestResult<Unit>
}
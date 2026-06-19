package com.brbx.domain.user.log_out.contract

import com.brbx.domain.model.DomainRequestResult

interface UserLogOutRepository {

    suspend fun logOut(): DomainRequestResult<Unit>
}
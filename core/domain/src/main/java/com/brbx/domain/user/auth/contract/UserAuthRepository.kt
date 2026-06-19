package com.brbx.domain.user.auth.contract

import com.brbx.domain.model.DomainRequestResult
import com.brbx.domain.user.auth.model.DomainAuthForm
import com.brbx.domain.user.auth.model.DomainToken

interface UserAuthRepository {

    suspend fun auth(form: DomainAuthForm): DomainRequestResult<DomainToken>
}
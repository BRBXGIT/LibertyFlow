package com.brbx.domain.user.auth.repository

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.auth.model.DomainAuthForm
import com.brbx.domain.user.auth.model.DomainToken

interface UserAuthRepository {

    suspend fun auth(form: DomainAuthForm): DomainRequestResult<DomainToken>
}
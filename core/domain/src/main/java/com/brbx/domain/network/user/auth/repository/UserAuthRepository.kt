package com.brbx.domain.network.user.auth.repository

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.auth.model.DomainAuthForm
import com.brbx.domain.network.user.auth.model.DomainToken

interface UserAuthRepository {

    suspend fun auth(form: DomainAuthForm): DomainRequestResult<DomainToken>
}
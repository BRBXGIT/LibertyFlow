package com.brbx.domain.network.user.auth.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.auth.model.DomainAuthForm
import com.brbx.domain.network.user.auth.model.DomainToken
import com.brbx.domain.network.user.auth.repository.UserAuthRepository

class UserAuthUseCase(
    private val repository: UserAuthRepository,
) {
    suspend operator fun invoke(form: DomainAuthForm): DomainRequestResult<DomainToken> =
        repository.auth(form)
}
package com.brbx.domain.user.auth.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.auth.model.DomainAuthForm
import com.brbx.domain.user.auth.model.DomainToken
import com.brbx.domain.user.auth.repository.UserAuthRepository

class UserAuthUseCase(
    private val repository: UserAuthRepository,
) {
    suspend operator fun invoke(form: DomainAuthForm): DomainRequestResult<DomainToken> =
        repository.auth(form)
}
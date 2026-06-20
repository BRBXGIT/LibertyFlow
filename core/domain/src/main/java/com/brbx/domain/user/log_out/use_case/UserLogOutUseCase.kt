package com.brbx.domain.user.log_out.use_case

import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.log_out.repository.UserLogOutRepository

class UserLogOutUseCase(
    private val repository: UserLogOutRepository,
) {
    suspend operator fun invoke(): DomainRequestResult<Unit> =
        repository.logOut()
}
package com.brbx.domain.network.user.log_out.use_case

import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.log_out.repository.UserLogOutRepository

class UserLogOutUseCase(
    private val repository: UserLogOutRepository,
) {
    suspend operator fun invoke(): DomainRequestResult<Unit> =
        repository.logOut()
}
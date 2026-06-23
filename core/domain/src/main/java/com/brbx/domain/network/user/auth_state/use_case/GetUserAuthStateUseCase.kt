package com.brbx.domain.network.user.auth_state.use_case

import com.brbx.domain.network.user.auth_state.model.UserAuthState
import com.brbx.domain.network.user.auth_state.repository.UserAuthStateRepository
import kotlinx.coroutines.flow.Flow

class GetUserAuthStateUseCase(
    private val repository: UserAuthStateRepository,
) {
    operator fun invoke(): Flow<UserAuthState> =
        repository.authState
}
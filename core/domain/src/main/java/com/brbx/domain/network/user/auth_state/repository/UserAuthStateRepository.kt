package com.brbx.domain.network.user.auth_state.repository

import com.brbx.domain.network.user.auth_state.model.UserAuthState
import kotlinx.coroutines.flow.Flow

interface UserAuthStateRepository {

    val authState: Flow<UserAuthState>
}
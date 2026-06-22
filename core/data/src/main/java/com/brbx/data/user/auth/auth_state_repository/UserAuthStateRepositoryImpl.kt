package com.brbx.data.user.auth.auth_state_repository

import com.brbx.domain.user.auth_state.model.UserAuthState
import com.brbx.domain.user.auth_state.repository.UserAuthStateRepository
import com.brbx.preferences.auth.AuthPrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserAuthStateRepositoryImpl(prefs: AuthPrefsManager) : UserAuthStateRepository {

    override val authState: Flow<UserAuthState> = prefs.token.map { token ->
        token.toUserAuthState()
    }

    private fun String?.toUserAuthState(): UserAuthState =
        when (this) {
            null -> UserAuthState.LoggedOut
            else -> UserAuthState.LoggedIn
        }
}
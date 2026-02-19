package com.example.common.vm_helpers.auth.component

import com.example.common.vm_helpers.auth.models.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthComponent {

    val authState: StateFlow<AuthState>
    fun observeAuth(scope: CoroutineScope, onUpdate: (AuthState) -> Unit)

    fun toggleAuthBS()
    fun onLoginChanged(login: String)
    fun onPasswordChanged(password: String)
    fun onErrorChanged(isError: Boolean)

    fun login(
        scope: CoroutineScope,
        onError: (Int, retry: () -> Unit) -> Unit,
    )
}
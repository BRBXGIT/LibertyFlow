package com.example.common.vm_helpers.auth.delegate

import com.example.common.vm_helpers.models.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthDelegate {

    val authState: StateFlow<AuthState>

    fun observeAuth(scope: CoroutineScope)
    fun toggleAuthBS()
    fun onLoginChanged(login: String)
    fun onPasswordChanged(password: String)
    fun onErrorChanged(isError: Boolean)

    fun login(
        scope: CoroutineScope,
        onError: (Int, retry: () -> Unit) -> Unit,
    )
}
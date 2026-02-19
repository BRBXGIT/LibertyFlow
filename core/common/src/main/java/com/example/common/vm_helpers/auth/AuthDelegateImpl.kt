package com.example.common.vm_helpers.auth

import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.data.domain.AuthRepo
import com.example.data.models.auth.TokenRequest
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.onError
import com.example.data.utils.network.network_caller.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthDelegateImpl @Inject constructor(
    private val authRepo: AuthRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): AuthDelegate {

    private val _authState = MutableStateFlow(AuthState())
    override val authState = _authState.asStateFlow()

    override fun observeAuth(scope: CoroutineScope) {
        authRepo.userAuthState
            .onEach { auth -> _authState.update { it.copy(userAuthState = auth) } }
            .launchIn(scope)
    }

    override fun toggleAuthBS() =
        _authState.update { it.toggleIsAuthBSVisible() }

    override fun onLoginChanged(login: String) =
        _authState.update { it.copy(login = login) }

    override fun onPasswordChanged(password: String) =
        _authState.update { it.copy(password = password) }

    override fun onErrorChanged(isError: Boolean) =
        _authState.update { it.copy(isError = isError) }

    override fun login(
        scope: CoroutineScope,
        onError: (Int, retry: () -> Unit) -> Unit,
    ) {
        val form = _authState.value
        scope.launch(dispatcherIo) {
            _authState.update { it.copy(isError = false) }
            authRepo.getToken(TokenRequest(form.login, form.password))
                .onSuccess {
                    it.token?.let { t -> authRepo.saveToken(t) }
                }
                .onError { error, msgRes ->
                    when(error) {
                        NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD ->
                            _authState.update { it.copy(isAuthBSVisible = true, isError = true) }
                        else -> onError(msgRes) { login(scope, onError) }
                    }
                }
        }
    }
}
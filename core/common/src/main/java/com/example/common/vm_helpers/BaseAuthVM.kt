package com.example.common.vm_helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.domain.AuthRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.TokenRequest
import com.example.data.utils.network.network_request.NetworkErrors
import com.example.data.utils.network.network_request.onError
import com.example.data.utils.network.network_request.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

abstract class BaseAuthVM(
    private val authRepo: AuthRepo,
    protected val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    /**
     * Exposes the current authentication state as a StateFlow.
     * Inheritors can use this to react to login/logout events.
     */
    val authState = authRepo.authState.toLazily(AuthState.LoggedOut)

    /**
     * Executes authentication request.
     * @param request Data containing credentials.
     * @param onStart Triggered when the network call begins.
     * @param onValidationError Triggered if credentials are wrong (401/403).
     * @param onError Triggered for generic network failures, provides a retry callback.
     */
    protected fun executeAuthentication(
        request: TokenRequest,
        onStart: () -> Unit,
        onSuccess: () -> Unit = {},
        onValidationError: () -> Unit,
        onError: suspend (messageRes: Int, onRetry: () -> Unit) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            onStart()
            authRepo.getToken(request)
                .onSuccess { uiToken ->
                    uiToken.token?.let { authRepo.saveToken(it) }
                    onSuccess()
                }
                .onError { error, messageRes ->
                    when (error) {
                        NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD -> onValidationError()
                        else -> onError(messageRes) {
                            executeAuthentication(request, onStart, onSuccess, onValidationError, onError)
                        }
                    }
                }
        }
    }
}
package com.example.common.vm_helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.domain.AuthRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.UiTokenRequest
import com.example.data.utils.remote.network_request.NetworkErrors
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

abstract class BaseAuthVM(
    private val authRepo: AuthRepo,
    private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    /**
     * Observes authentication state and updates UI.
     */
    protected fun observeAuthState(onCollect: (AuthState) -> Unit) {
        viewModelScope.launch(dispatcherIo) {
            authRepo.authState.collect { authState ->
                onCollect(authState)
            }
        }
    }

    /**
     * Requests auth token using email/password.
     * Shows snackbar with retry on unknown errors.
     */
    protected fun getAuthToken(
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onIncorrectData: () -> Unit,
        onAnyError: suspend (String, onRetry: () -> Unit) -> Unit,
        request: UiTokenRequest
    ) {
        viewModelScope.launch(dispatcherIo) {
            onStart()

            authRepo.getToken(request)
                .onSuccess { uiToken ->
                    authRepo.saveToken(uiToken.token!!)
                    onSuccess()
                }
                .onError { error, message ->
                    when(error) {
                        NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD -> onIncorrectData()
                        else -> onAnyError(message) { getAuthToken(onStart, onSuccess, onIncorrectData, onAnyError, request) }
                    }
                }
        }
    }
}
package com.example.common.vm_helpers.auth.component

import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.auth.models.AuthState
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

/**
 * Concrete implementation of [AuthComponent] responsible for managing the authentication state
 * and coordinating login operations between the UI and [AuthRepo].
 *
 * This delegate encapsulates the [AuthState] using [MutableStateFlow] to ensure a single
 * source of truth for the login UI.
 *
 * @property authRepo The repository handling remote and local authentication data operations.
 * @property dispatcherIo The coroutine dispatcher optimized for I/O tasks, used for network calls.
 */
class AuthComponentImpl @Inject constructor(
    private val authRepo: AuthRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): AuthComponent {

    private val _authState = MutableStateFlow(AuthState())
    override val authState = _authState.asStateFlow()

    override fun observeAuth(scope: CoroutineScope, onUpdate: (AuthState) -> Unit) {
        authRepo.userAuthState
            .onEach { auth -> _authState.update { it.copy(userAuthState = auth) } }
            .launchIn(scope)

        authState
            .onEach { state -> onUpdate(state) }
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

    /**
     * Executes the login process.
     * * This function switches to the [dispatcherIo], clears previous errors, and requests
     * a token from the [authRepo]. On success, the token is persisted. On failure, it
     * handles specific business logic for incorrect credentials or triggers a retry callback.
     *
     * @param scope The scope used to launch the asynchronous login request.
     * @param onError A lambda triggered on network errors. It provides the error message
     * resource ID and a retry function to re-attempt the login.
     */
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
                        NetworkErrors.INCORRECT_CREDENTIALS ->
                            _authState.update { it.copy(isAuthBSVisible = true, isError = true) }
                        else -> onError(msgRes) { login(scope, onError) }
                    }
                }
        }
    }
}
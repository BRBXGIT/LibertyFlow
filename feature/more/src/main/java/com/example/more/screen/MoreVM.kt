package com.example.more.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.toWhileSubscribed
import com.example.data.domain.AuthRepo
import com.example.data.utils.remote.network_request.NetworkErrors
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.more.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RETRY = "Retry"

private val SuccessLogoutLabel = R.string.success_logout_label

@HiltViewModel
class MoreVM @Inject constructor(
    private val authRepo: AuthRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(MoreState())
    val state =_state.toWhileSubscribed(MoreState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // --- Intents ---
    fun sendIntent(intent: MoreIntent) {
        when(intent) {
            // Ui
            MoreIntent.ToggleLogoutDialog -> _state.update { it.toggleLogoutAD() }

            // Data
            MoreIntent.Logout -> logout()
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Data ---
    private fun logout() {
        viewModelScope.launch(dispatcherIo) {
            authRepo.logout()
                .onSuccess {
                    authRepo.clearToken()
                    sendEffect(
                        effect = UiEffect.ShowSimpleSnackbar(
                            messageRes = SuccessLogoutLabel
                        )
                    )
                }
                .onError { error, messageRes ->
                    sendEffect(
                        effect = when(error) {
                            NetworkErrors.UNAUTHORIZED -> UiEffect.ShowSimpleSnackbar(
                                messageRes = messageRes
                            )
                            else -> UiEffect.ShowSnackbarWithAction(
                                messageRes = messageRes,
                                actionLabel = RETRY,
                                action = { logout() }
                            )
                        }
                    )
                }
        }
    }
}
package com.example.onboarding.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.OnboardingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the Onboarding flow.
 * * This ViewModel follows a unidirectional data flow (UDF) pattern:
 * - **State**: Managed via [MutableStateFlow], exposed as a lazy flow.
 * - **Intents**: Handled via [sendIntent] to trigger state changes or side effects.
 * - **Effects**: One-time UI events (like navigation) handled via a [Channel].
 *
 * @property onboardingRepo The repository handling persistence of onboarding data.
 * @property dispatcherIo The injected IO dispatcher for background operations,
 * identified by [LibertyFlowDispatcher.IO].
 */
@HiltViewModel
class OnboardingVM @Inject constructor(
    private val onboardingRepo: OnboardingRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.toLazily(OnboardingState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // --- Intents ---
    fun sendIntent(intent: OnboardingIntent) {
        when(intent) {
            OnboardingIntent.SaveOnboardingCompleted -> saveOnboardingCompleted()
            OnboardingIntent.UpdateTriedToAskPermission -> _state.update { it.copy(triedToAskPermission = true) }
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Data ---
    private fun saveOnboardingCompleted() = viewModelScope.launch(dispatcherIo) {
        onboardingRepo.saveOnboardingCompleted()
    }
}
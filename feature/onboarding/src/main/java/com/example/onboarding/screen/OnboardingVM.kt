package com.example.onboarding.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.domain.OnboardingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingVM @Inject constructor(
    private val onboardingRepo: OnboardingRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun sendIntent(intent: OnboardingIntent) {
        when(intent) {
            OnboardingIntent.SaveOnboardingCompleted -> saveOnboardingCompleted()
        }
    }

    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    private fun saveOnboardingCompleted() = viewModelScope.launch(dispatcherIo) {
        onboardingRepo.saveOnboardingCompleted()
    }
}
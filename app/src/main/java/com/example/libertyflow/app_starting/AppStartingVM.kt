package com.example.libertyflow.app_starting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.vm_helpers.toEagerly
import com.example.data.domain.OnboardingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppStartingVM @Inject constructor(
    private val onboardingRepo: OnboardingRepo
): ViewModel() {

    private val _appStartingState = MutableStateFlow(AppStartingState())
    val appStartingState = _appStartingState.toEagerly(AppStartingState())

    init {
        observeOnboarding()
    }

    private fun observeOnboarding() {
        viewModelScope.launch {
            onboardingRepo.onboardingState.collect { value ->
                _appStartingState.update { it.copy(onboardingCompleted = value) }
            }
        }
    }
}
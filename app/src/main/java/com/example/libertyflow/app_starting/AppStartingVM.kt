package com.example.libertyflow.app_starting

import androidx.lifecycle.ViewModel
import com.example.common.vm_helpers.toEagerly
import com.example.data.domain.OnboardingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ViewModel responsible for determining the application's starting destination.
 * * It observes the [OnboardingRepo] to check if the user has previously completed
 * the onboarding process, allowing the UI to decide whether to show the
 * Onboarding screens or the Main Home screen.
 *
 * @property onboardingRepo The data source for user preferences and onboarding status.
 */
@HiltViewModel
class AppStartingVM @Inject constructor(
    private val onboardingRepo: OnboardingRepo
): ViewModel() {

    val appStartingState = onboardingRepo.onboardingState
        .map { AppStartingState(it) }
        .toEagerly(AppStartingState())
}
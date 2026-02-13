package com.example.data.data

import com.example.data.domain.OnboardingRepo
import com.example.data.models.onboarding.OnboardingState
import com.example.local.onboarding.OnboardingPrefsManager
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnboardingRepoImpl @Inject constructor(
    private val onboardingPrefsManager: OnboardingPrefsManager
): OnboardingRepo {

    override val onboardingState = onboardingPrefsManager
        .isOnboardingCompleted
        .map { value ->
            when(value) {
                true -> OnboardingState.Completed
                false -> OnboardingState.NotCompleted
                null -> OnboardingState.NotCompleted
            }
        }

    override suspend fun saveOnboardingCompleted() =
        onboardingPrefsManager.saveOnboardingCompleted()
}
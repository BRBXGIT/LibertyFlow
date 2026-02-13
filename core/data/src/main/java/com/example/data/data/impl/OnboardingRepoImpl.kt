package com.example.data.data.impl

import com.example.data.domain.OnboardingRepo
import com.example.data.models.onboarding.OnboardingState
import com.example.local.onboarding.OnboardingPrefsManager
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [OnboardingRepo] managing the user's first-launch experience state.
 * Interfaces with [OnboardingPrefsManager] for persistent storage.
 */
class OnboardingRepoImpl @Inject constructor(
    private val onboardingPrefsManager: OnboardingPrefsManager
): OnboardingRepo {

    /**
     * A Flow that emits the current [OnboardingState].
     * Defaults to [OnboardingState.NotCompleted] if the preference is unset or false.
     */
    override val onboardingState = onboardingPrefsManager
        .isOnboardingCompleted
        .map { value ->
            when(value) {
                true -> OnboardingState.Completed
                false -> OnboardingState.NotCompleted
                null -> OnboardingState.NotCompleted
            }
        }

    /**
     * Persists the completion of the onboarding process.
     */
    override suspend fun saveOnboardingCompleted() =
        onboardingPrefsManager.saveOnboardingCompleted()
}
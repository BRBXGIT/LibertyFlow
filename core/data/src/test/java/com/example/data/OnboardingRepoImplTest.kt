package com.example.data

import app.cash.turbine.test
import com.example.data.data.impl.OnboardingRepoImpl
import com.example.data.domain.OnboardingRepo
import com.example.data.models.onboarding.OnboardingState
import com.example.local.onboarding.OnboardingPrefsManager
import com.example.unit.base.base.BaseUnitTest
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OnboardingRepoImplTest: BaseUnitTest() {

    private val onboardingPrefsManager = mockk<OnboardingPrefsManager>()
    private lateinit var repo: OnboardingRepo

    @Test
    fun `onboardingState emits correct states based on prefs`() = runTest {
        val testCases = mapOf(
            true to OnboardingState.Completed,
            false to OnboardingState.NotCompleted,
            null to OnboardingState.NotCompleted
        )

        testCases.forEach { (inputValue, expectedState) ->
            every { onboardingPrefsManager.isOnboardingCompleted } returns flowOf(inputValue)

            createRepo()

            repo.onboardingState.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    // --- Helpers ---
    private fun createRepo() { // Used cause with @Before method test's couldn't be passed
        repo = OnboardingRepoImpl(onboardingPrefsManager)
    }
}
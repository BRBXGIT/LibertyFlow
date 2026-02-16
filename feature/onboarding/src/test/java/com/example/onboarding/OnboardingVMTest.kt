@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.onboarding

import app.cash.turbine.test
import com.example.data.domain.OnboardingRepo
import com.example.onboarding.screen.OnboardingIntent
import com.example.onboarding.screen.OnboardingVM
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class OnboardingVMTest {

    private val repo = mockk<OnboardingRepo>()
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var vm: OnboardingVM

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vm = OnboardingVM(repo, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `state updates correctly`() = runTest {
        vm.state.test {
            skipItems(1)
            vm.sendIntent(OnboardingIntent.UpdateTriedToAskPermission)

            val item = awaitItem()
            assertTrue(item.triedToAskPermission)
        }
    }

    @Test
    fun `vm asks repo for update`() = runTest {
        coEvery { repo.saveOnboardingCompleted() } just Runs
        vm.sendIntent(OnboardingIntent.SaveOnboardingCompleted)
        coVerify { repo.saveOnboardingCompleted() }
    }
}
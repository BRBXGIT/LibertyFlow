@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.more

import app.cash.turbine.test
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.domain.AuthRepo
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.NetworkResult
import com.example.design_system.utils.CommonStrings
import com.example.more.screen.MoreIntent
import com.example.more.screen.MoreVM
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
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

class MoreVMTest {

    private val repo = mockk<AuthRepo>()
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var vm: MoreVM

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vm = MoreVM(repo, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // --- Logout tests ---
    @Test
    fun `logout clears token and send effect on success result`() = runTest {
        coEvery { repo.logout() } returns NetworkResult.Success(Unit)
        coEvery { repo.clearToken() } just Runs

        vm.effects.test {
            vm.sendIntent(MoreIntent.Logout)

            val effect = awaitItem()
            assertTrue(effect is UiEffect.ShowSimpleSnackbar)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repo.clearToken() }
    }

    @Test
    fun `logout send effect on error result with UNAUTHORIZED`() = runTest {
        val errorRes = 123
        coEvery { repo.logout() } returns NetworkResult.Error(
            error = NetworkErrors.UNAUTHORIZED,
            messageRes = errorRes
        )

        vm.effects.test {
            vm.sendIntent(MoreIntent.Logout)

            val effect = awaitItem()
            assertTrue(effect is UiEffect.ShowSimpleSnackbar)
            assertEquals(errorRes, (effect as UiEffect.ShowSimpleSnackbar).messageRes)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `logout sends snackbar with retry action on generic error`() = runTest {
        val errorRes = 54321
        coEvery { repo.logout() } returns NetworkResult.Error(
            error = NetworkErrors.SERVER_ERROR,
            messageRes = errorRes
        )

        vm.effects.test {
            vm.sendIntent(MoreIntent.Logout)

            val effect = awaitItem()

            // Check type
            assertTrue(effect is UiEffect.ShowSnackbarWithAction)
            val actionEffect = effect as UiEffect.ShowSnackbarWithAction

            assertEquals(errorRes, actionEffect.messageRes)
            assertEquals(CommonStrings.RETRY, actionEffect.actionLabel)

            // Retry test
            coEvery { repo.logout() } returns NetworkResult.Success(Unit)
            coEvery { repo.clearToken() } just Runs

            actionEffect.action?.invoke()

            coVerify(exactly = 2) { repo.logout() }
            coVerify(exactly = 1) { repo.clearToken() }

            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- State tests
    @Test
    fun `state updates correctly`() = runTest {
        vm.state.test {
            skipItems(1)
            vm.sendIntent(MoreIntent.ToggleLogoutDialog)

            val item = awaitItem()
            assertTrue(item.isLogoutADVisible)
        }
    }
}
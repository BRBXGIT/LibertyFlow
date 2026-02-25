package com.example.common

import app.cash.turbine.test
import com.example.common.vm_helpers.auth.component.AuthComponentImpl
import com.example.data.domain.AuthRepo
import com.example.data.models.auth.Token
import com.example.data.models.auth.UserAuthState
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthComponentTest {

    private val authRepo: AuthRepo = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var authComponent: AuthComponentImpl

    @Before
    fun setup() {
        authComponent = AuthComponentImpl(
            authRepo = authRepo,
            dispatcherIo = testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `initial state should be default`() = runTest {
        authComponent.authState.test {
            val initialState = awaitItem()
            assertEquals("", initialState.login)
            assertEquals("", initialState.password)
            assertFalse(initialState.isError)
            assertFalse(initialState.isAuthBSVisible)
            assertEquals(UserAuthState.LoggedOut, initialState.userAuthState)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginChanged should update state`() = runTest {
        authComponent.authState.test {
            skipItems(1)

            authComponent.onLoginChanged("new_user")

            val state = awaitItem()
            assertEquals("new_user", state.login)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPasswordChanged should update state`() = runTest {
        authComponent.authState.test {
            skipItems(1)

            authComponent.onPasswordChanged("secret_123")

            assertEquals("secret_123", awaitItem().password)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleAuthBS should change visibility`() = runTest {
        authComponent.authState.test {
            val initial = awaitItem()
            assertFalse(initial.isAuthBSVisible)

            authComponent.toggleAuthBS()
            assertTrue(awaitItem().isAuthBSVisible)

            authComponent.toggleAuthBS()
            assertFalse(awaitItem().isAuthBSVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login SUCCESS - should clear error and save token`() = runTest {
        val testToken = "valid_token"
        val response = mockk<Token> { every { token } returns testToken }

        coEvery { authRepo.getToken(any()) } returns NetworkResult.Success(response)

        authComponent.login(backgroundScope) { _, _ -> }

        coVerify(exactly = 1) { authRepo.getToken(any()) }
        coVerify(exactly = 1) { authRepo.saveToken(testToken) }

        authComponent.authState.test {
            assertFalse(awaitItem().isError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login INCORRECT_CREDENTIALS - should show error and keep BS visible`() = runTest {
        coEvery { authRepo.getToken(any()) } returns NetworkResult.Error(
            error = NetworkErrors.INCORRECT_CREDENTIALS,
            messageRes = 1
        )

        authComponent.login(backgroundScope) { _, _ -> }

        authComponent.authState.test {
            val state = awaitItem()
            assertTrue(state.isError)
            assertTrue(state.isAuthBSVisible)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { authRepo.saveToken(any()) }
    }

    @Test
    fun `login NETWORK_ERROR - should trigger onError callback and support retry`() = runTest {
        val errorMsgRes = 500
        var capturedRetry: (() -> Unit)? = null

        coEvery { authRepo.getToken(any()) } returns NetworkResult.Error(
            error = NetworkErrors.SERVER_ERROR,
            messageRes = errorMsgRes
        )

        authComponent.login(backgroundScope) { resId, retry ->
            assertEquals(errorMsgRes, resId)
            capturedRetry = retry
        }

        coVerify(exactly = 1) { authRepo.getToken(any()) }

        assertNotNull(capturedRetry)
        capturedRetry?.invoke()

        coVerify(exactly = 2) { authRepo.getToken(any()) }
    }
}
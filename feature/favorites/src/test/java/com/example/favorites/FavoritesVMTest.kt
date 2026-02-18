package com.example.favorites

import app.cash.turbine.test
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.Token
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.NetworkResult
import com.example.favorites.screen.FavoritesIntent
import com.example.favorites.screen.FavoritesVM
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesVMTest {

    private val authRepo = mockk<AuthRepo>()
    private val favoritesRepo = mockk<FavoritesRepo>()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var vm: FavoritesVM

    private val authStateFlow = MutableStateFlow<AuthState>(AuthState.LoggedOut)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        every { authRepo.authState } returns authStateFlow

        vm = FavoritesVM(authRepo, favoritesRepo, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // --- Tests for Initialization & Auth Observation ---
    @Test
    fun `init should subscribe to authState and update VM state`() = runTest {
        vm.state.test {
            val initialState = awaitItem()
            assertEquals(AuthState.LoggedOut, initialState.authState)

            authStateFlow.emit(AuthState.LoggedIn)

            val loggedInState = awaitItem()
            assertEquals(AuthState.LoggedIn, loggedInState.authState)
        }
    }

    // --- Tests for Simple UI Intents ---
    @Test
    fun `ToggleIsAuthBSVisible intent should toggle boolean state`() = runTest {
        vm.state.test {
            assertFalse(awaitItem().authForm.isAuthBSVisible)
            vm.sendIntent(FavoritesIntent.ToggleIsAuthBSVisible)
            assertTrue(awaitItem().authForm.isAuthBSVisible)
            vm.sendIntent(FavoritesIntent.ToggleIsAuthBSVisible)
            assertFalse(awaitItem().authForm.isAuthBSVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `UpdateQuery intent should update query in searchForm`() = runTest {
        vm.state.test {
            skipItems(1)

            val query = "Test Query"
            vm.sendIntent(FavoritesIntent.UpdateQuery(query))
            assertEquals(query, awaitItem().searchForm.query)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ToggleIsSearching intent should toggle searching state`() = runTest {
        vm.state.test {
            assertFalse(awaitItem().searchForm.isSearching)
            vm.sendIntent(FavoritesIntent.ToggleIsSearching)
            assertTrue(awaitItem().searchForm.isSearching)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `UpdateAuthForm intent should update login and password`() = runTest {
        val email = "test@example.com"
        val pass = "password123"

        vm.state.test {
            skipItems(1)

            vm.sendIntent(FavoritesIntent.UpdateAuthForm(FavoritesIntent.UpdateAuthForm.AuthField.Email(email)))
            assertEquals(email, awaitItem().authForm.login)

            vm.sendIntent(FavoritesIntent.UpdateAuthForm(FavoritesIntent.UpdateAuthForm.AuthField.Password(pass)))
            assertEquals(pass, awaitItem().authForm.password)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Tests for Authentication Logic (Complex) ---
    @Test
    fun `GetTokens intent success should save token and clear errors`() = runTest {
        coEvery { authRepo.authState } returns flowOf(AuthState.LoggedOut)

        val token = "123"
        val successResult = NetworkResult.Success(Token(token))
        coEvery { authRepo.getToken(any()) } returns successResult
        coEvery { authRepo.saveToken(token) } just Runs

        vm.state.test {
            vm.sendIntent(FavoritesIntent.GetTokens)

            advanceUntilIdle()

            val final = awaitItem()

            coVerify(exactly = 1) { authRepo.saveToken(token) }
            assertFalse(final.authForm.isError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GetTokens intent validation error should set error state and show BS`() = runTest {
        val errorResult = NetworkResult.Error(
            error = NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD,
            messageRes = 0
        )
        coEvery { authRepo.getToken(any()) } returns errorResult

        vm.state.test {
            val login = "wrong"
            val password = "pwd"
            vm.sendIntent(FavoritesIntent.UpdateAuthForm(FavoritesIntent.UpdateAuthForm.AuthField.Email(login)))
            vm.sendIntent(FavoritesIntent.UpdateAuthForm(FavoritesIntent.UpdateAuthForm.AuthField.Password(password)))

            vm.sendIntent(FavoritesIntent.GetTokens)

            advanceUntilIdle()

            val final = expectMostRecentItem()
            assertTrue(final.authForm.isError)
            assertTrue(final.authForm.isAuthBSVisible)
            coVerify(exactly = 0) { authRepo.saveToken(any()) } // Don't save token
        }
    }

    @Test
    fun `GetTokens intent generic error should emit effect`() = runTest {
        val errorMsgRes = 123
        val errorResult = NetworkResult.Error(
            error = NetworkErrors.REQUEST_TIMEOUT,
            messageRes = errorMsgRes
        )
        coEvery { authRepo.getToken(any()) } returns errorResult

        vm.effects.test {
            val login = "user"
            val password = "pwd"
            vm.sendIntent(FavoritesIntent.UpdateAuthForm(FavoritesIntent.UpdateAuthForm.AuthField.Email(login)))
            vm.sendIntent(FavoritesIntent.UpdateAuthForm(FavoritesIntent.UpdateAuthForm.AuthField.Password(password)))

            vm.sendIntent(FavoritesIntent.GetTokens)

            advanceUntilIdle()

            val final = awaitItem()
            assertTrue(final is UiEffect.ShowSnackbarWithAction)
            assertEquals(errorMsgRes, (final as UiEffect.ShowSnackbarWithAction).messageRes)
        }
    }
}
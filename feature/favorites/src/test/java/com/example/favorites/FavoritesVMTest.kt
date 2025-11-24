@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites

import app.cash.turbine.test
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.UiToken
import com.example.data.models.auth.UiTokenRequest
import com.example.data.utils.remote.network_request.NetworkErrors
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.design_system.components.snackbars.SnackbarController
import com.example.favorites.screen.FavoritesIntent
import com.example.favorites.screen.FavoritesState
import com.example.favorites.screen.FavoritesVM
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavoritesVMTest {

    private lateinit var vm: FavoritesVM

    private val favoritesRepo: FavoritesRepo = mockk()
    private val authRepo: AuthRepo = mockk()
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        coEvery { authRepo.authState } returns flowOf(AuthState.LoggedIn)

        vm = FavoritesVM(favoritesRepo, authRepo, dispatcher)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state updates correctly`() = runTest {
        vm.favoritesState.test {
            vm.sendIntent(FavoritesIntent.ToggleIsAuthBSVisible)
            vm.sendIntent(FavoritesIntent.ToggleIsSearching)

            vm.sendIntent(FavoritesIntent.SetIsLoading(true))
            vm.sendIntent(FavoritesIntent.SetIsError(true))

            vm.sendIntent(FavoritesIntent.UpdateQuery("query"))
            vm.sendIntent(FavoritesIntent.UpdateEmail("email"))
            vm.sendIntent(FavoritesIntent.UpdatePassword("password"))

            advanceUntilIdle()

            val expected = FavoritesState(
                isLoggedIn = AuthState.LoggedIn,
                isLoading = true,
                isError = true,
                isAuthBSVisible = true,
                email = "email",
                password = "password",
                query = "query",
                isSearching = true
            )

            val after = expectMostRecentItem()

            assertEquals(expected, after)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAuthToken save token on success`() = runTest {
        coEvery { authRepo.saveToken("token") } just Runs
        coEvery { authRepo.getToken(UiTokenRequest("", "")) } returns
                NetworkResult.Success(UiToken("token"))

        vm.sendIntent(FavoritesIntent.GetTokens)

        advanceUntilIdle()

        coVerify { authRepo.saveToken("token") }
    }

    @Test
    fun `getAuthToken make snackbar on error`() = runTest {
        SnackbarController.events.test {
            coEvery { authRepo.getToken(UiTokenRequest("", "")) } returns
                    NetworkResult.Error(NetworkErrors.SERVER_ERROR, "SERVER ERROR")

            vm.sendIntent(FavoritesIntent.GetTokens)

            advanceUntilIdle()

            val after = awaitItem()

            assertEquals("SERVER ERROR", after.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAuthToken updates state on INCORRECT_EMAIL_OR_PASSWORD_ERROR`() = runTest {
        vm.favoritesState.test {
            coEvery { authRepo.getToken(UiTokenRequest("", "")) } returns
                    NetworkResult.Error(NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD, "INCORRECT EMAIL OR PASSWORD")

            vm.sendIntent(FavoritesIntent.GetTokens)

            advanceUntilIdle()

            val after = expectMostRecentItem()
            assertTrue(after.isPasswordOrEmailIncorrect)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
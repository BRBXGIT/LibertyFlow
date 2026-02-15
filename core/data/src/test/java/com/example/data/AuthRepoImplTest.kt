package com.example.data

import app.cash.turbine.test
import com.example.data.data.impl.AuthRepoImpl
import com.example.data.domain.AuthRepo
import com.example.data.models.auth.AuthState
import com.example.data.utils.network.network_caller.NetworkCaller
import com.example.local.auth.AuthPrefsManager
import com.example.network.auth.api.AuthApi
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class AuthRepoImplTest {

    private val networkCaller = NetworkCaller()
    private val authApi = mockk<AuthApi>()
    private val authPrefsManager = mockk<AuthPrefsManager>()
    private lateinit var repo: AuthRepo

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `authState emits LoggedIn when token is present`() = runTest {
        val token = "token"
        every { authPrefsManager.token } returns flowOf(token)

        createRepo()

        repo.authState.test {
            assertEquals(AuthState.LoggedIn, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `authState emits LoggedOut when token is null`() = runTest {
        val token = null
        every { authPrefsManager.token } returns flowOf(token)

        createRepo()

        repo.authState.test {
            assertEquals(AuthState.LoggedOut, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `token saves with the Bearer prefix`() = runTest {
        every { authPrefsManager.token } returns flowOf(null)
        coEvery { authPrefsManager.saveToken(any()) } just Runs

        val bearer = "Bearer"
        val token = "token"
        createRepo()
        repo.saveToken(token)

        coVerify { authPrefsManager.saveToken("$bearer $token") }
    }

    

    // --- Helpers ---
    private fun createRepo() { // Used cause with @Before method test's couldn't be passed
        repo = AuthRepoImpl(networkCaller, authApi, authPrefsManager)
    }
}
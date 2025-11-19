@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home

import app.cash.turbine.test
import com.example.data.domain.CatalogRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.data.models.releases.anime_id.UiAnimeId
import com.example.data.utils.remote.network_request.NetworkErrors
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.design_system.components.snackbars.SnackbarController
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeVM
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeVMTest {

    private lateinit var vm: HomeVM

    private var releasesRepo: ReleasesRepo = mockk(relaxed = true)
    private var catalogRepo: CatalogRepo = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(dispatcher)
        vm = HomeVM(releasesRepo, catalogRepo, dispatcher)
        advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state updates correctly`() = runTest {
        vm.homeState.test {
            skipItems(1)

            vm.sendIntent(HomeIntent.UpdateIsSearching)
            vm.sendIntent(HomeIntent.UpdateQuery("query"))
            vm.sendIntent(HomeIntent.UpdateIsLoading(true))

            advanceUntilIdle()

            val after = awaitItem()

            assertEquals("query", after.query)
            assertTrue(after.isLoading)
            assertTrue(after.isSearching)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchLatestReleases updates state on success`() = runTest {
        val result = NetworkResult.Success(ArrayList<UiAnimeItem>())
        coEvery { releasesRepo.getLatestAnimeReleases() } returns result

        vm = HomeVM(releasesRepo, catalogRepo, dispatcher)
        vm.homeState.test {
            val after = awaitItem()
            assertEquals(result.data, after.latestReleases)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchLatestReleases create snackbar on error`() = runTest {
        val result = NetworkResult.Error(NetworkErrors.INTERNET, "INTERNET")
        coEvery { releasesRepo.getLatestAnimeReleases() } returns result

        vm = HomeVM(releasesRepo, catalogRepo, dispatcher)
        SnackbarController.events.test {
            skipItems(1)

            val event = awaitItem()
            assertEquals("INTERNET", event.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // TODO FIX TESTS
    @Test
    fun `getRandomAnime updates state on success`() = runTest {
        val result = NetworkResult.Success(UiAnimeId(id = 1))
        coEvery { releasesRepo.getRandomAnime() } returns result

        vm.homeState.test {
            vm.sendIntent(HomeIntent.GetRandomAnime)
            advanceUntilIdle()

            val after = awaitItem()
            assertEquals(1, after.randomAnimeId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRandomAnime create snackbar on error`() = runTest {
        val result = NetworkResult.Error(NetworkErrors.INTERNET, "INTERNET")
        coEvery { releasesRepo.getRandomAnime() } returns result

        vm = HomeVM(releasesRepo, catalogRepo, dispatcher)
        advanceUntilIdle()

        SnackbarController.events.test {
            cancelAndIgnoreRemainingEvents()
        }

        SnackbarController.events.test {
            vm.sendIntent(HomeIntent.GetRandomAnime)
            advanceUntilIdle()

            val event = awaitItem()
            assertEquals("INTERNET", event.message)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
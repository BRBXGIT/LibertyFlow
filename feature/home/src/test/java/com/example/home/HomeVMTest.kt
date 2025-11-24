@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home

import app.cash.turbine.test
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters
import com.example.data.models.common.request.request_parameters.UiYear
import com.example.data.models.releases.anime_id.UiAnimeId
import com.example.data.utils.remote.network_request.NetworkErrors
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.design_system.components.snackbars.SnackbarController
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeState
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeVMTest {

    private lateinit var vm: HomeVM

    private val releasesRepo: ReleasesRepo = mockk(relaxed = true)
    private val catalogRepo: CatalogRepo = mockk(relaxed = true)
    private val genresRepo: GenresRepo = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(dispatcher)
        vm = HomeVM(releasesRepo, catalogRepo, genresRepo, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state updates correctly`() = runTest {
        vm.homeState.test {
            vm.sendIntent(HomeIntent.ToggleSearching)
            vm.sendIntent(HomeIntent.ToggleFiltersBottomSheet)

            vm.sendIntent(HomeIntent.SetLoading(true))
            vm.sendIntent(HomeIntent.SetError(true))

            vm.sendIntent(HomeIntent.UpdateQuery("query"))

            vm.sendIntent(HomeIntent.AddGenre(UiGenre(id = 1, name = "Genre")))
            vm.sendIntent(HomeIntent.AddGenre(UiGenre(id = 2, name = "Genre2")))
            vm.sendIntent(HomeIntent.RemoveGenre(UiGenre(id = 2, name = "Genre2")))
            vm.sendIntent(HomeIntent.AddSeason(Season.AUTUMN))
            vm.sendIntent(HomeIntent.AddSeason(Season.WINTER))
            vm.sendIntent(HomeIntent.RemoveSeason(Season.WINTER))
            vm.sendIntent(HomeIntent.UpdateFromYear(10))
            vm.sendIntent(HomeIntent.UpdateToYear(20))
            vm.sendIntent(HomeIntent.UpdateSorting(Sorting.YEAR_ASC))
            vm.sendIntent(HomeIntent.ToggleIsOngoing)

            advanceUntilIdle()

            val afterState = HomeState(
                isError = true,
                isLoading = true,
                isSearching = true,
                isFiltersBSVisible = true,
                request = UiFullRequestParameters(
                    seasons = listOf(Season.AUTUMN),
                    years = UiYear(from = 10, to = 20),
                    sorting = Sorting.YEAR_ASC,
                    publishStatuses = listOf(PublishStatus.IS_ONGOING),
                    search = "query",
                    genres = listOf(UiGenre(id = 1, name = "Genre"))
                )
            )

            val after = expectMostRecentItem()

            assertEquals(afterState, after)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRandomAnime updates state on success`() = runTest {
        coEvery { releasesRepo.getRandomAnime() } returns
                NetworkResult.Success(UiAnimeId(id = 1))

        vm.homeState.test {
            skipItems(1)
            vm.sendIntent(HomeIntent.GetRandomAnime)

            advanceUntilIdle()

            val after = awaitItem()
            assertEquals(1, after.randomAnimeId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRandomAnime makes snackbar`() = runTest {
        coEvery { releasesRepo.getRandomAnime() } returns
                NetworkResult.Error(NetworkErrors.SERVER_ERROR, "Server error")

        SnackbarController.events.test {
            vm.sendIntent(HomeIntent.GetRandomAnime)

            advanceUntilIdle()

            val after = awaitItem()
            assertEquals("Server error", after.message)
        }
    }

    @Test
    fun `getGenres updates state on success`() = runTest {
        coEvery { genresRepo.getGenres() } returns
                NetworkResult.Success(listOf(UiGenre(id = 1, name = "")))

        vm.homeState.test {
            skipItems(1)
            vm.sendIntent(HomeIntent.GetGenres)

            advanceUntilIdle()

            val after = awaitItem()
            assertTrue(after.genres.isNotEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGenres makes snackbar`() = runTest {
        coEvery { genresRepo.getGenres() } returns
                NetworkResult.Error(NetworkErrors.SERVER_ERROR, "Server error")

        SnackbarController.events.test {
            vm.sendIntent(HomeIntent.GetGenres)

            advanceUntilIdle()

            val after = awaitItem()
            assertEquals("Server error", after.message)
        }
    }
}
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home

import app.cash.turbine.test
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.common.Genre
import com.example.data.models.releases.anime_id.AnimeId
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.NetworkResult
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeVM
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeVMTest {

    private val releasesRepo = mockk<ReleasesRepo>(relaxed = true)
    private val catalogRepo = mockk<CatalogRepo>(relaxed = true)
    private val genresRepo = mockk<GenresRepo>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var vm: HomeVM

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        vm = HomeVM(releasesRepo, catalogRepo, genresRepo, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // --- Network ---
    @Test
    fun `getRandomAnime send effect on success`() = runTest {
        val id = 123
        coEvery { releasesRepo.getRandomAnime() } returns NetworkResult.Success(AnimeId(id))

        assertEffectEmitted(HomeIntent.GetRandomAnime) { effect ->
            assertTrue(effect is UiEffect.Navigate && effect.route == AnimeDetailsRoute(id))
        }
    }

    @Test
    fun `getRandomAnime send effect on error`() = runTest {
        mockError { releasesRepo.getRandomAnime() }

        assertSnackbarErrorEmitted(HomeIntent.GetRandomAnime)
    }

    @Test
    fun `getGenres updates state on success`() = runTest {
        val genres = listOf(Genre(1, "Action"))
        coEvery { genresRepo.getGenres() } returns NetworkResult.Success(genres)

        vm.state.test {
            skipItems(1)
            vm.sendIntent(HomeIntent.GetGenres)
            assertEquals(genres, awaitItem().genresState.genres)
        }
    }

    @Test
    fun `getGenres send effect on error`() = runTest {
        mockError { genresRepo.getGenres() }

        assertSnackbarErrorEmitted(HomeIntent.GetGenres)
    }

    // --- State ---
    @Test
    fun `ToggleSearching intent should update isSearching`() = runTest {
        vm.state.test {
            val initialState = awaitItem()
            assertFalse(initialState.isSearching)

            vm.sendIntent(HomeIntent.ToggleSearching)
            assertTrue(awaitItem().isSearching)

            vm.sendIntent(HomeIntent.ToggleSearching)
            assertFalse(awaitItem().isSearching)
        }
    }

    @Test
    fun `ToggleFiltersBottomSheet intent should update isFiltersBSVisible`() = runTest{
        vm.state.test {
            awaitItem()
            vm.sendIntent(HomeIntent.ToggleFiltersBottomSheet)

            val state = awaitItem()
            assertTrue(state.filtersState.isFiltersBSVisible)
        }
    }

    @Test
    fun `UpdateQuery intent should update search string in nested request`() = runTest {
        vm.state.test {
            awaitItem()
            val query = "Attack on Titan"
            vm.sendIntent(HomeIntent.UpdateQuery(query))

            assertEquals(query, awaitItem().filtersState.request.search)
        }
    }

    @Test
    fun `Add and Remove Genre intents should update genres list`() = runTest {
        val genre = Genre(id = 1, name = "Action")
        vm.state.test {
            awaitItem()

            // Add
            vm.sendIntent(HomeIntent.AddGenre(genre))
            val addedState = awaitItem()
            assertTrue(addedState.filtersState.request.genres.contains(genre))

            // Remove
            vm.sendIntent(HomeIntent.RemoveGenre(genre))
            val removedState = awaitItem()
            assertFalse(removedState.filtersState.request.genres.contains(genre))
        }
    }

    @Test
    fun `Update Year range intents should update specific year fields`() = runTest {
        vm.state.test {
            awaitItem()

            vm.sendIntent(HomeIntent.UpdateFromYear(2020))
            assertEquals(2020, awaitItem().filtersState.request.years.from)

            vm.sendIntent(HomeIntent.UpdateToYear(2024))
            assertEquals(2024, awaitItem().filtersState.request.years.to)
        }
    }

    // --- Helpers ---
    private fun mockError(messageRes: Int = 123, block: suspend () -> NetworkResult<*>) {
        coEvery { block() } returns NetworkResult.Error(
            error = NetworkErrors.SERVER_ERROR,
            messageRes = messageRes
        )
    }

    private suspend fun assertEffectEmitted(
        intent: HomeIntent,
        check: (UiEffect) -> Unit
    ) {
        vm.effects.test {
            vm.sendIntent(intent)
            check(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun assertSnackbarErrorEmitted(intent: HomeIntent, expectedRes: Int = 123) {
        assertEffectEmitted(intent) { effect ->
            assertTrue(effect is UiEffect.ShowSnackbarWithAction)
            assertEquals(expectedRes, (effect as UiEffect.ShowSnackbarWithAction).messageRes)
        }
    }
}
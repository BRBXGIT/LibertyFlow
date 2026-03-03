package com.example.home

import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.filters.component.FiltersComponent
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.common.Genre
import com.example.data.models.releases.anime_id.AnimeId
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.NetworkResult
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeVM
import com.example.unit.base.base.BaseUnitTest
import com.example.unit.base.flow.testState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeVMTest: BaseUnitTest() {

    private val filtersComponent = mockk<FiltersComponent>()

    private val releasesRepo = mockk<ReleasesRepo>()
    private val catalogRepo = mockk<CatalogRepo>()
    private val genresRepo = mockk<GenresRepo>()

    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var vm: HomeVM

    @Before
    fun setup() {
        coEvery { filtersComponent.observeFilters(any(), any()) } just Runs

        vm = HomeVM(
            filtersComponent = filtersComponent,
            releasesRepo = releasesRepo,
            catalogRepo = catalogRepo,
            genresRepo = genresRepo,
            dispatcherIo = testDispatcher
        )
    }

    @Test
    fun `SetLoading updates state`() = runTest {
        vm.state.testState {
            vm.sendIntent(HomeIntent.SetLoading(true))
            assertTrue(awaitItem().loadingState.isLoading)
        }
    }

    @Test
    fun `SetError updates state`() = runTest {
        vm.state.testState {
            vm.sendIntent(HomeIntent.SetError(true))
            assertTrue(awaitItem().loadingState.isError)
        }
    }

    @Test
    fun `SendEffect sends effect`() = runTest {
        val messageRes = 123
        vm.effects.testState(skipInitial = false) {
            vm.sendEffect(UiEffect.ShowSimpleSnackbar(messageRes))

            val item = awaitItem()
            assert(item is UiEffect.ShowSimpleSnackbar)
            assertEquals(messageRes, (item as UiEffect.ShowSimpleSnackbar).messageRes)
        }
    }

    @Test
    fun `getRandomAnime navigates on success`() = runTest {
        val animeId = AnimeId(1)
        coEvery { releasesRepo.getRandomAnime() } returns NetworkResult.Success(animeId)

        vm.effects.testState(skipInitial = false) {
            vm.sendIntent(HomeIntent.GetRandomAnime)

            val item = awaitItem()
            assert(item is UiEffect.Navigate)
        }
    }

    @Test
    fun `getRandomAnime makes snackbar on error`() = runTest {
        val messageRes = 123
        val error = NetworkErrors.SERVER_ERROR
        coEvery { releasesRepo.getRandomAnime() } returns NetworkResult.Error(error, messageRes)

        vm.effects.testState(skipInitial = false) {
            vm.sendIntent(HomeIntent.GetRandomAnime)

            val item = awaitItem()
            assert(item is UiEffect.ShowSnackbarWithAction)
            assertEquals(messageRes, (item as UiEffect.ShowSnackbarWithAction).messageRes)
        }
    }

    @Test
    fun `getGenres updates state`() = runTest {
        val genres = listOf(Genre(id = 1, name = "Seinen"))
        coEvery { genresRepo.getGenres() } returns NetworkResult.Success(genres)

        vm.state.testState {
            vm.sendIntent(HomeIntent.GetGenres)

            advanceUntilIdle()

            val item = awaitItem()
            assertEquals(genres, item.genresState.genres)
        }
    }

    @Test
    fun `getGenres makes snackbar on error`() = runTest {
        val messageRes = 123
        val error = NetworkErrors.SERVER_ERROR
        coEvery { genresRepo.getGenres() } returns NetworkResult.Error(error, messageRes)

        vm.effects.testState(skipInitial = false) {
            vm.sendIntent(HomeIntent.GetGenres)

            val item = awaitItem()
            assert(item is UiEffect.ShowSnackbarWithAction)
            assertEquals(messageRes, (item as UiEffect.ShowSnackbarWithAction).messageRes)
        }
    }
}
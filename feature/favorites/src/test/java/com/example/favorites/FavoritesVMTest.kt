package com.example.favorites

import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.auth.component.AuthComponent
import com.example.common.vm_helpers.filters.component.FiltersComponent
import com.example.data.domain.FavoritesRepo
import com.example.favorites.screen.FavoritesIntent
import com.example.favorites.screen.FavoritesVM
import com.example.unit.base.base.BaseUnitTest
import com.example.unit.base.flow.testState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoritesVMTest: BaseUnitTest() {

    private val authComponent = mockk<AuthComponent>()
    private val filtersComponent = mockk<FiltersComponent>()
    private val favoritesRepo = mockk<FavoritesRepo>()

    private lateinit var vm: FavoritesVM

    @Before
    fun setup() {
        coEvery { authComponent.observeAuth(any(), any()) } just Runs
        coEvery { filtersComponent.observeFilters(any(), any()) } just Runs

        vm = FavoritesVM(authComponent, filtersComponent, favoritesRepo)
    }

    @Test
    fun `SetIsLoading updates state`() = runTest {
        vm.state.testState {
            vm.sendIntent(FavoritesIntent.SetIsLoading(true))
            assertTrue(awaitItem().loadingState.isLoading)
        }
    }

    @Test
    fun `SetIsError updates state`() = runTest {
        vm.state.testState {
            vm.sendIntent(FavoritesIntent.SetIsError(true))
            assertTrue(awaitItem().loadingState.isError)
        }
    }

    @Test
    fun `sendEffect sends effect`() = runTest {
        vm.effects.testState(skipInitial = false) {
            val messageRes = 123
            vm.sendEffect(UiEffect.ShowSimpleSnackbar(messageRes))

            val item = awaitItem()
            assert(item is UiEffect.ShowSimpleSnackbar)
            assertEquals(messageRes, (item as UiEffect.ShowSimpleSnackbar).messageRes)
        }
    }
}
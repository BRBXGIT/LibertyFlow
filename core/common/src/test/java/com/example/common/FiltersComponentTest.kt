@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.common

import com.example.common.vm_helpers.filters.component.FiltersComponentImpl
import com.example.common.vm_helpers.filters.models.FiltersState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.unit.base.base.BaseUnitTest
import com.example.unit.base.flow.testState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FiltersComponentTest: BaseUnitTest() {

    private lateinit var filtersComponent: FiltersComponentImpl

    @Before
    fun setup() {
        filtersComponent = FiltersComponentImpl()
    }

    @Test
    fun `toggleIsSearching should flip boolean flag`() = runTest {
        filtersComponent.filtersState.testState {

            filtersComponent.toggleIsSearching()
            assertTrue(awaitItem().isSearching)

            filtersComponent.toggleIsSearching()
            assertFalse(awaitItem().isSearching)
        }
    }

    @Test
    fun `updateQuery should update search parameter`() = runTest {
        val query = "Naruto"
        filtersComponent.filtersState.testState {
            filtersComponent.updateQuery(query)
            assertEquals(query, awaitItem().requestParameters.search)
        }
    }

    @Test
    fun `addGenre and removeGenre should update genres list`() = runTest {
        val genre = Genre(id = 1, name = "Action")

        filtersComponent.filtersState.testState {

            filtersComponent.addGenre(genre)
            val stateWithGenre = awaitItem()
            assertTrue(stateWithGenre.requestParameters.genres.contains(genre))

            filtersComponent.removeGenre(genre)
            val stateWithoutGenre = awaitItem()
            assertFalse(stateWithoutGenre.requestParameters.genres.contains(genre))

        }
    }

    @Test
    fun `handleToggleOngoing should toggle IS_ONGOING status`() = runTest {
        filtersComponent.filtersState.testState {

            filtersComponent.toggleIsOngoing()
            val stateEnabled = awaitItem()
            assertEquals(listOf(PublishStatus.IS_ONGOING), stateEnabled.requestParameters.publishStatuses)

            filtersComponent.toggleIsOngoing()
            val stateDisabled = awaitItem()
            assertTrue(stateDisabled.requestParameters.publishStatuses.isEmpty())

        }
    }

    @Test
    fun `updateFromYear and updateToYear should update only respective fields`() = runTest {
        filtersComponent.filtersState.testState {
            filtersComponent.updateFromYear(2000)
            assertEquals(2000, awaitItem().requestParameters.years.from)

            filtersComponent.updateToYear(2024)
            val state = awaitItem()
            assertEquals(2000, state.requestParameters.years.from)
            assertEquals(2024, state.requestParameters.years.to)
        }
    }

    @Test
    fun `addSeason and removeSeason should manage seasons collection`() = runTest {
        val winter = Season.WINTER

        filtersComponent.filtersState.testState {

            filtersComponent.addSeason(winter)
            assertTrue(awaitItem().requestParameters.seasons.contains(winter))

            filtersComponent.removeSeason(winter)
            assertFalse(awaitItem().requestParameters.seasons.contains(winter))
        }
    }

    @Test
    fun `observeFilters should emit updates to callback`() = runTest {
        val updates = MutableStateFlow(FiltersState())

        filtersComponent.updateQuery("Bleach")
        filtersComponent.toggleFiltersBS()

        filtersComponent.observeFilters(backgroundScope) {
            updates.value = it
        }

        runCurrent()

        assertEquals("Bleach", updates.value.requestParameters.search)
        assertTrue(updates.value.isFiltersBSVisible)
    }

    @Test
    fun `updateSorting should change sorting criteria`() = runTest {
        val newSorting = Sorting.CREATED_AT_ASC

        filtersComponent.filtersState.testState {

            filtersComponent.updateSorting(newSorting)
            assertEquals(newSorting, awaitItem().requestParameters.sorting)
        }
    }
}
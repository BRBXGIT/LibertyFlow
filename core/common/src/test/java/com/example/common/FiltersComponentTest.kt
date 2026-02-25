package com.example.common

import app.cash.turbine.test
import com.example.common.vm_helpers.filters.component.FiltersComponentImpl
import com.example.common.vm_helpers.filters.models.FiltersState
import com.example.data.models.common.common.Genre
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FiltersComponentTest {

    private lateinit var filtersComponent: FiltersComponentImpl

    @Before
    fun setup() {
        filtersComponent = FiltersComponentImpl()
    }

    @Test
    fun `toggleIsSearching should flip boolean flag`() = runTest {
        filtersComponent.filtersState.test {
            skipItems(1)

            filtersComponent.toggleIsSearching()
            assertTrue(awaitItem().isSearching)

            filtersComponent.toggleIsSearching()
            assertFalse(awaitItem().isSearching)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateQuery should update search parameter`() = runTest {
        val query = "Naruto"
        filtersComponent.filtersState.test {
            skipItems(1)
            filtersComponent.updateQuery(query)
            assertEquals(query, awaitItem().requestParameters.search)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addGenre and removeGenre should update genres list`() = runTest {
        val genre = Genre(id = 1, name = "Action")

        filtersComponent.filtersState.test {
            skipItems(1)

            filtersComponent.addGenre(genre)
            val stateWithGenre = awaitItem()
            assertTrue(stateWithGenre.requestParameters.genres.contains(genre))

            filtersComponent.removeGenre(genre)
            val stateWithoutGenre = awaitItem()
            assertFalse(stateWithoutGenre.requestParameters.genres.contains(genre))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleToggleOngoing should toggle IS_ONGOING status`() = runTest {
        filtersComponent.filtersState.test {
            skipItems(1)

            filtersComponent.toggleIsOngoing()
            val stateEnabled = awaitItem()
            assertEquals(listOf(PublishStatus.IS_ONGOING), stateEnabled.requestParameters.publishStatuses)

            filtersComponent.toggleIsOngoing()
            val stateDisabled = awaitItem()
            assertTrue(stateDisabled.requestParameters.publishStatuses.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateFromYear and updateToYear should update only respective fields`() = runTest {
        filtersComponent.filtersState.test {
            skipItems(1)

            filtersComponent.updateFromYear(2000)
            assertEquals(2000, awaitItem().requestParameters.years.from)

            filtersComponent.updateToYear(2024)
            val state = awaitItem()
            assertEquals(2000, state.requestParameters.years.from)
            assertEquals(2024, state.requestParameters.years.to)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addSeason and removeSeason should manage seasons collection`() = runTest {
        val winter = Season.WINTER

        filtersComponent.filtersState.test {
            skipItems(1)

            filtersComponent.addSeason(winter)
            assertTrue(awaitItem().requestParameters.seasons.contains(winter))

            filtersComponent.removeSeason(winter)
            assertFalse(awaitItem().requestParameters.seasons.contains(winter))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeFilters should emit updates to callback`() = runTest {
        val updates = MutableStateFlow(FiltersState())

        updates.test {
            filtersComponent.updateQuery("Bleach")
            filtersComponent.toggleFiltersBS()

            filtersComponent.observeFilters(backgroundScope) {
                updates.value = it
            }

            assertEquals("Bleach", updates.value.requestParameters.search)
            assertTrue(updates.value.isFiltersBSVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateSorting should change sorting criteria`() = runTest {
        val newSorting = Sorting.CREATED_AT_ASC

        filtersComponent.filtersState.test {
            skipItems(1)
            filtersComponent.updateSorting(newSorting)
            assertEquals(newSorting, awaitItem().requestParameters.sorting)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
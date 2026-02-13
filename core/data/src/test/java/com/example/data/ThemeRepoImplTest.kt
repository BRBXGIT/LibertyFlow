package com.example.data

import app.cash.turbine.test
import com.example.data.data.impl.ThemeRepoImpl
import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.TabType
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class ThemeRepoImplTest {

    private val themePrefsManager = mockk<ThemePrefsManager>()
    private lateinit var repo: ThemeRepo

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `libertyFlowTheme returns correct values when prefs are null`() = runTest {
        coEvery { themePrefsManager.theme } returns flowOf(null)
        coEvery { themePrefsManager.useExpressive } returns flowOf(null)
        coEvery { themePrefsManager.colorSystem } returns flowOf(null)
        coEvery { themePrefsManager.tabType } returns flowOf(null)

        createRepo()

        repo.libertyFlowTheme.test {
            val item = awaitItem()

            assertEquals(ThemeValue.SYSTEM, item.userThemePreference)
            assertTrue(item.useExpressive)
            assertEquals(ColorSchemeValue.DARK_LAVENDER_SCHEME, item.activeColorScheme)
            assertEquals(TabType.M3, item.tabType)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `libertyFlowTheme returns correct values when prefs are not null`() = runTest {
        coEvery { themePrefsManager.theme } returns flowOf("LIGHT")
        coEvery { themePrefsManager.useExpressive } returns flowOf(false)
        coEvery { themePrefsManager.colorSystem } returns flowOf("LIGHT_LAVENDER_SCHEME")
        coEvery { themePrefsManager.tabType } returns flowOf("Chips")

        createRepo()

        repo.libertyFlowTheme.test { 
            val item = awaitItem()

            assertEquals(ThemeValue.LIGHT, item.userThemePreference)
            assertFalse(item.useExpressive)
            assertEquals(ColorSchemeValue.LIGHT_LAVENDER_SCHEME, item.activeColorScheme)
            assertEquals(TabType.Chips, item.tabType)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Helpers ---
    private fun createRepo() {
        repo = ThemeRepoImpl(themePrefsManager)
    }
}
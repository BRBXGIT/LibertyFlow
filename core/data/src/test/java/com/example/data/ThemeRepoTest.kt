package com.example.data

import app.cash.turbine.test
import com.example.data.data.ThemeRepoImpl
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ThemeRepoTest {

    private lateinit var themePrefsManager: ThemePrefsManager
    private lateinit var repo: ThemeRepoImpl

    private lateinit var themeFlow: MutableStateFlow<String?>
    private lateinit var colorFlow: MutableStateFlow<String?>
    private lateinit var useExpressiveFlow: MutableStateFlow<Boolean?>

    @Before
    fun setup() {
        themePrefsManager = mockk(relaxed = true)

        themeFlow = MutableStateFlow(null)
        colorFlow = MutableStateFlow(null)
        useExpressiveFlow = MutableStateFlow(null)

        every { themePrefsManager.theme } returns themeFlow
        every { themePrefsManager.colorSystem } returns colorFlow
        every { themePrefsManager.useExpressive } returns useExpressiveFlow

        repo = ThemeRepoImpl(themePrefsManager)
    }

    @Test
    fun `theme emits default SYSTEM when prefs is null`() = runTest {
        repo.theme.test {
            val value = awaitItem()
            assertEquals(ThemeValue.SYSTEM, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `theme emits saved value`() = runTest {
        themeFlow.value = "DARK"

        repo.theme.test {
            val value = awaitItem()
            assertEquals(ThemeValue.DARK, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `useExpressive emits false when prefs is null`() = runTest {
        repo.useExpressive.test {
            val value = awaitItem()
            assertFalse(value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `useExpressive emits saved value`() = runTest {
        useExpressiveFlow.value = true

        repo.useExpressive.test {
            val value = awaitItem()
            assertTrue(value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `colorSystem returns default LIGHT when prefs is null and theme LIGHT`() = runTest {
        themeFlow.value = "LIGHT"
        colorFlow.value = null

        repo.colorSystem(isSystemDarkMode = false).test {
            val value = awaitItem()
            assertEquals(ColorSchemeValue.LIGHT_LAVENDER_SCHEME, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `colorSystem returns default DARK when prefs is null and theme DARK`() = runTest {
        themeFlow.value = "DARK"
        colorFlow.value = null

        repo.colorSystem(isSystemDarkMode = true).test {
            val value = awaitItem()
            assertEquals(ColorSchemeValue.DARK_LAVENDER_SCHEME, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `colorSystem switches LIGHT_Tacos to DARK_Tacos when theme changes to DARK`() = runTest {
        themeFlow.value = "DARK"
        colorFlow.value = ColorSchemeValue.LIGHT_TACOS_SCHEME.name

        repo.colorSystem(isSystemDarkMode = true).test {
            val value = awaitItem()
            assertEquals(ColorSchemeValue.DARK_TACOS_SCHEME, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `colorSystem switches DARK_Cherry to LIGHT_Cherry when theme changes to LIGHT`() = runTest {
        themeFlow.value = "LIGHT"
        colorFlow.value = ColorSchemeValue.DARK_CHERRY_SCHEME.name

        repo.colorSystem(isSystemDarkMode = false).test {
            val value = awaitItem()
            assertEquals(ColorSchemeValue.LIGHT_CHERRY_SCHEME, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `colorSystem does not change if already matching theme`() = runTest {
        themeFlow.value = "DARK"
        colorFlow.value = ColorSchemeValue.DARK_LAVENDER_SCHEME.name

        repo.colorSystem(isSystemDarkMode = true).test {
            val value = awaitItem()
            assertEquals(ColorSchemeValue.DARK_LAVENDER_SCHEME, value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveTheme calls prefsManager`() = runTest {
        coEvery { themePrefsManager.saveTheme(any()) } just Runs

        repo.saveTheme(ThemeValue.LIGHT)

        coVerify { themePrefsManager.saveTheme("LIGHT") }
    }

    @Test
    fun `saveColorSystem calls prefsManager`() = runTest {
        coEvery { themePrefsManager.saveColorSystem(any()) } just Runs

        repo.saveColorSystem(ColorSchemeValue.LIGHT_TACOS_SCHEME)

        coVerify { themePrefsManager.saveColorSystem("LIGHT_TACOS_SCHEME") }
    }

    @Test
    fun `saveUseExpressive calls prefsManager`() = runTest {
        coEvery { themePrefsManager.saveUseExpressive(any()) } just Runs

        repo.saveUseExpressive(true)

        coVerify { themePrefsManager.saveUseExpressive(true) }
    }
}
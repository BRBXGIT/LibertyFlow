@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.settings

import app.cash.turbine.test
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.domain.ThemeRepo
import com.example.data.models.player.PlayerSettings
import com.example.data.models.player.VideoQuality
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.TabType
import com.example.data.models.theme.ThemeValue
import com.example.settings.screen.SettingsIntent
import com.example.settings.screen.SettingsVM
import com.example.unit.base.base.BaseUnitTest
import com.example.unit.base.flow.testState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SettingsVMTest: BaseUnitTest() {

    private val themeRepo = mockk<ThemeRepo>()
    private val playerSettingsRepo = mockk<PlayerSettingsRepo>()

    private lateinit var vm: SettingsVM

    private val themeFlow = MutableStateFlow(LibertyFlowTheme())
    private val playerSettingsFlow = MutableStateFlow(PlayerSettings())

    private val testDispatcher = mainDispatcherRule.testDispatcher

    @Before
    fun setup() {
        every { themeRepo.libertyFlowTheme } returns themeFlow
        every { playerSettingsRepo.playerSettings } returns playerSettingsFlow

        vm = SettingsVM(themeRepo, playerSettingsRepo, testDispatcher)
    }

    // --- Initial ---
    @Test
    fun `initial state should match repository values`() = runTest {
        vm.state.testState(skipInitial = false) {
            val state = awaitItem()
            assertEquals(themeFlow.value, state.themeSettings)
            assertEquals(playerSettingsFlow.value, state.playerSettings)
        }
    }

    // --- Theme Intent ---
    @Test
    fun `intent SetTheme should call themeRepo saveTheme`() = runTest {
        val latest = ThemeValue.DARK
        coEvery { themeRepo.saveTheme(latest) } just Runs

        vm.sendIntent(SettingsIntent.SetTheme(latest))

        advanceUntilIdle()

        coVerify { themeRepo.saveTheme(latest) }
    }

    @Test
    fun `intent SetColorScheme should call themeRepo saveColorSystem`() = runTest {
        val latest = ColorSchemeValue.DARK_LAVENDER_SCHEME
        coEvery { themeRepo.saveColorSystem(latest) } just Runs

        vm.sendIntent(SettingsIntent.SetColorScheme(latest))

        advanceUntilIdle()

        coVerify { themeRepo.saveColorSystem(latest) }
    }

    @Test
    fun `intent ToggleUseExpressive should call repo with negated current value`() = runTest {
        val latest = false
        coEvery { themeRepo.saveUseExpressive(latest) } just Runs

        themeFlow.value = LibertyFlowTheme(useExpressive = true)

        advanceUntilIdle()
        
        vm.sendIntent(SettingsIntent.ToggleUseExpressive)

        advanceUntilIdle()

        coVerify { themeRepo.saveUseExpressive(latest) }
    }

    @Test
    fun `intent ToggleTabType should cycle through tab types`() = runTest {
        coEvery { themeRepo.saveTab(any()) } just Runs

        // 1. M3 -> Tablet
        themeFlow.value = LibertyFlowTheme(tabType = TabType.M3)
        advanceUntilIdle()

        vm.sendIntent(SettingsIntent.ToggleTabType)
        advanceUntilIdle()
        coVerify { themeRepo.saveTab(TabType.Tablet) }

        // 2. Tablet -> Chips
        themeFlow.value = LibertyFlowTheme(tabType = TabType.Tablet)
        advanceUntilIdle()

        vm.sendIntent(SettingsIntent.ToggleTabType)
        advanceUntilIdle()
        coVerify { themeRepo.saveTab(TabType.Chips) }

        // 3. Chips -> M3
        themeFlow.value = LibertyFlowTheme(tabType = TabType.Chips)
        advanceUntilIdle()

        vm.sendIntent(SettingsIntent.ToggleTabType)
        advanceUntilIdle()
        coVerify { themeRepo.saveTab(TabType.M3) }
    }

    // --- Player Intent ---
    @Test
    fun `intent SetQuality should call playerSettingsRepo`() = runTest {
        val latest = VideoQuality.FHD
        coEvery { playerSettingsRepo.saveQuality(latest) } just Runs

        vm.sendIntent(SettingsIntent.SetQuality(latest))

        advanceUntilIdle()

        coVerify { playerSettingsRepo.saveQuality(latest) }
    }

    @Test
    fun `intent ToggleAutoPlay should call repo with negated value`() = runTest {
        val latest = true
        coEvery { playerSettingsRepo.saveAutoPlay(latest) } just Runs

        playerSettingsFlow.value = PlayerSettings(autoPlay = false)
        
        vm.sendIntent(SettingsIntent.ToggleAutoPlay)

        advanceUntilIdle()

        coVerify { playerSettingsRepo.saveAutoPlay(latest) }
    }

    @Test
    fun `intent ToggleIsCropped should call repo with negated value`() = runTest {
        val latest = true
        coEvery { playerSettingsRepo.saveIsCopped(latest) } just Runs

        playerSettingsFlow.value = PlayerSettings(isCropped = false)

        vm.sendIntent(SettingsIntent.ToggleIsCropped)

        advanceUntilIdle()

        coVerify { playerSettingsRepo.saveIsCopped(latest) }
    }

    @Test
    fun `intent ToggleShowSkipOpeningButton should call repo with negated value`() = runTest {
        val latest = true
        coEvery { playerSettingsRepo.saveShowSkipOpeningButton(latest) } just Runs

        playerSettingsFlow.value = PlayerSettings(showSkipOpeningButton = false)

        vm.sendIntent(SettingsIntent.ToggleShowSkipOpeningButton)

        advanceUntilIdle()

        coVerify { playerSettingsRepo.saveShowSkipOpeningButton(latest) }
    }

    // --- UI State ---
    @Test
    fun `intent ToggleIsQualityBSVisible should update state`() = runTest {
        vm.state.testState(skipInitial = true) {
            vm.sendIntent(SettingsIntent.ToggleIsQualityBSVisible)
            
            val state = awaitItem()
            assertTrue(state.isQualityBSVisible)

            vm.sendIntent(SettingsIntent.ToggleIsQualityBSVisible)
            assertEquals(false, awaitItem().isQualityBSVisible)
        }
    }

    // --- Observe Prefs ---
    @Test
    fun `state should update when themeRepo emits new value`() = runTest {
        vm.state.testState(skipInitial = true) {
            val newTheme = LibertyFlowTheme(useExpressive = true)
            themeFlow.value = newTheme
            
            val updatedState = awaitItem()
            assertEquals(newTheme, updatedState.themeSettings)
        }
    }

    // --- Effects ---
    @Test
    fun `sendEffect should emit effect to effects flow`() = runTest {
        val expectedEffect = UiEffect.ShowSimpleSnackbar(1)
        
        vm.effects.test {
            vm.sendEffect(expectedEffect)
            assertEquals(expectedEffect, awaitItem())
        }
    }
}
package com.example.data

import app.cash.turbine.test
import com.example.data.data.impl.PlayerSettingsRepoImpl
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.VideoQuality
import com.example.local.player_settings.PlayerSettingsPrefsManager
import com.example.unit.base.base.BaseUnitTest
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PlayerSettingsRepoImplTest: BaseUnitTest() {

    private val playerSettingsPrefsManager = mockk<PlayerSettingsPrefsManager>()
    private lateinit var repo: PlayerSettingsRepo

    @Test
    fun `playerSettings emits combined data with default values`() = runTest {
        every { playerSettingsPrefsManager.quality } returns flowOf(null)
        every { playerSettingsPrefsManager.showSkipOpeningButton } returns flowOf(null)
        every { playerSettingsPrefsManager.autoSkipOpening } returns flowOf(null)
        every { playerSettingsPrefsManager.autoPlay } returns flowOf(null)
        every { playerSettingsPrefsManager.isCropped } returns flowOf(null)

        createRepo()

        repo.playerSettings.test {
            val settings = awaitItem()

            assertEquals(VideoQuality.SD, settings.quality)
            assertEquals(true, settings.showSkipOpeningButton)
            assertEquals(false, settings.autoSkipOpening)
            assertEquals(true, settings.autoPlay)
            assertEquals(false, settings.isCropped)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `playerSettings maps quality strings correctly`() = runTest {
        val qualityFlow = MutableStateFlow<String?>("FHD")
        every { playerSettingsPrefsManager.quality } returns qualityFlow
        // Fix another fields
        every { playerSettingsPrefsManager.showSkipOpeningButton } returns flowOf(true)
        every { playerSettingsPrefsManager.autoSkipOpening } returns flowOf(false)
        every { playerSettingsPrefsManager.autoPlay } returns flowOf(true)
        every { playerSettingsPrefsManager.isCropped } returns flowOf(false)

        createRepo()

        repo.playerSettings.test {
            assertEquals(VideoQuality.FHD, awaitItem().quality)

            qualityFlow.value = "HD"
            assertEquals(VideoQuality.HD, awaitItem().quality)

            qualityFlow.value = "unknown"
            assertEquals(VideoQuality.SD, awaitItem().quality)
        }
    }

    // --- Helpers ---
    private fun createRepo() {
        repo = PlayerSettingsRepoImpl(playerSettingsPrefsManager)
    }
}
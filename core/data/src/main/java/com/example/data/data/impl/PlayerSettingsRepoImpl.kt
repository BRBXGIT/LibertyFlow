package com.example.data.data.impl

import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.PlayerSettings
import com.example.data.models.player.VideoQuality
import com.example.local.player_settings.PlayerSettingsPrefsManager
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Implementation of [PlayerSettingsRepo] that manages video player configurations.
 * Coordinates multiple preference streams into a single [PlayerSettings] state.
 */
class PlayerSettingsRepoImpl @Inject constructor(
    private val playerSettingsPrefsManager: PlayerSettingsPrefsManager
): PlayerSettingsRepo {

    /**
     * A Flow that combines individual preference emissions into a unified [PlayerSettings] object.
     * Provides default fallback values for unset preferences.
     */
    override val playerSettings = combine(
        flow = playerSettingsPrefsManager.quality,
        flow2 = playerSettingsPrefsManager.showSkipOpeningButton,
        flow3 = playerSettingsPrefsManager.autoSkipOpening,
        flow4 = playerSettingsPrefsManager.autoPlay,
        flow5 = playerSettingsPrefsManager.isCropped
    ) { quality, showSkipOpeningButton, autoSkipOpening, autoPlay, isCropped ->
        PlayerSettings(
            quality = quality.toVideoQuality(),
            showSkipOpeningButton = showSkipOpeningButton ?: true,
            autoSkipOpening = autoSkipOpening ?: false,
            autoPlay = autoPlay ?: true,
            isCropped = isCropped ?: false
        )
    }

    override suspend fun saveQuality(quality: VideoQuality) =
        playerSettingsPrefsManager.saveQuality(quality.name)

    override suspend fun saveShowSkipOpeningButton(show: Boolean) =
        playerSettingsPrefsManager.saveShowSkipOpeningButton(show)

    override suspend fun saveAutoSkipOpening(skip: Boolean) =
        playerSettingsPrefsManager.saveAutoSkipOpening(skip)

    override suspend fun saveAutoPlay(autoPlay: Boolean) =
        playerSettingsPrefsManager.saveAutoPlay(autoPlay)

    override suspend fun saveIsCopped(isCropped: Boolean) =
        playerSettingsPrefsManager.saveIsCopped(isCropped)

    // --- Mappers ---
    private val fhd = "FHD"
    private val hd = "HD"

    private fun String?.toVideoQuality() = when(this) {
        fhd -> VideoQuality.FHD
        hd -> VideoQuality.HD
        else -> VideoQuality.SD
    }
}
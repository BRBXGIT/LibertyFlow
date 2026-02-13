package com.example.data.data.impl

import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.PlayerSettings
import com.example.data.models.player.VideoQuality
import com.example.local.player_settings.PlayerSettingsPrefsManager
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PlayerSettingsRepoImpl @Inject constructor(
    private val playerSettingsPrefsManager: PlayerSettingsPrefsManager
): PlayerSettingsRepo {

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
}

private const val FHD = "FHD"
private const val HD = "HD"

private fun String?.toVideoQuality() = when(this) {
    FHD -> VideoQuality.FHD
    HD -> VideoQuality.HD
    else -> VideoQuality.SD
}
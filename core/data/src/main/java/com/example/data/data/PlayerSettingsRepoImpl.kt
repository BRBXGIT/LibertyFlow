package com.example.data.data

import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.PlayerSettings
import com.example.data.models.player.VideoQuality
import com.example.local.player_settings.PlayerPrefsManager
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PlayerSettingsRepoImpl @Inject constructor(
    private val playerPrefsManager: PlayerPrefsManager
): PlayerSettingsRepo {

    override val playerSettings = combine(
        flow = playerPrefsManager.quality,
        flow2 = playerPrefsManager.showSkipOpeningButton,
        flow3 = playerPrefsManager.autoSkipOpening,
        flow4 = playerPrefsManager.autoPlay,
        flow5 = playerPrefsManager.isCropped
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
        playerPrefsManager.saveQuality(quality.name)

    override suspend fun saveShowSkipOpeningButton(show: Boolean) =
        playerPrefsManager.saveShowSkipOpeningButton(show)

    override suspend fun saveAutoSkipOpening(skip: Boolean) =
        playerPrefsManager.saveAutoSkipOpening(skip)

    override suspend fun saveAutoPlay(autoPlay: Boolean) =
        playerPrefsManager.saveAutoPlay(autoPlay)

    override suspend fun saveIsCopped(isCropped: Boolean) =
        playerPrefsManager.saveIsCopped(isCropped)
}

private const val FHD = "FHD"
private const val HD = "HD"

private fun String?.toVideoQuality() = when(this) {
    FHD -> VideoQuality.FHD
    HD -> VideoQuality.HD
    else -> VideoQuality.SD
}
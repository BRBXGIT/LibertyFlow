package com.example.data.data

import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.VideoQuality
import com.example.local.player_settings.PlayerPrefsManager
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerSettingsRepoImpl @Inject constructor(
    private val playerPrefsManager: PlayerPrefsManager
): PlayerSettingsRepo {

    override val quality = playerPrefsManager.quality.map { it.toVideoQuality() }

    override val showSkipOpeningButton = playerPrefsManager.showSkipOpeningButton.map { it ?: true }

    override val autoSkipOpening = playerPrefsManager.autoSkipOpening.map { it ?: false }

    override val autoPlay = playerPrefsManager.autoPlay.map { it ?: true }

    override suspend fun saveQuality(quality: VideoQuality) =
        playerPrefsManager.saveQuality(quality.name)

    override suspend fun saveShowSkipOpeningButton(show: Boolean) =
        playerPrefsManager.saveShowSkipOpeningButton(show)

    override suspend fun saveAutoSkipOpening(skip: Boolean) =
        playerPrefsManager.saveAutoSkipOpening(skip)

    override suspend fun saveAutoPlay(autoPlay: Boolean) =
        playerPrefsManager.saveAutoPlay(autoPlay)
}

private fun String?.toVideoQuality() = when(this) {
    "FHD" -> VideoQuality.FHD
    "HD" -> VideoQuality.HD
    else -> VideoQuality.SD
}
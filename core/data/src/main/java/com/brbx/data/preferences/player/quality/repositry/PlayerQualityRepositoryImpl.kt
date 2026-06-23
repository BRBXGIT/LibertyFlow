package com.brbx.data.preferences.player.quality.repositry

import com.brbx.data.common.map.toEnumOrDefault
import com.brbx.domain.preferences.player.quality.model.VideoQuality
import com.brbx.domain.preferences.player.quality.repository.PlayerQualityRepository
import com.brbx.preferences.player.manager.quality.PlayerQualityPrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PlayerQualityRepositoryImpl(
    private val prefs: PlayerQualityPrefsManager,
) : PlayerQualityRepository {

    override val value: Flow<VideoQuality> = prefs.value.map {
        it.toEnumOrDefault(defaultValue = VideoQuality.Sd)
    }

    override suspend fun set(value: VideoQuality) {
        prefs.save(value.name)
    }
}
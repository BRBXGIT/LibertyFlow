package com.brbx.preferences.player.manager.quality

import kotlinx.coroutines.flow.Flow

interface PlayerQualityPrefsManager {

    val quality: Flow<String?>

    suspend fun saveQuality(quality: String)
}
package com.brbx.data.preferences.player.fullscreen.repository

import com.brbx.data.preferences.common.map.booleanNotNull
import com.brbx.domain.preferences.player.fullscreen.repository.PlayerFullScreenRepository
import com.brbx.preferences.player.manager.fullscreen.PlayerFullScreenPrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerFullScreenRepositoryImpl(
    private val prefs: PlayerFullScreenPrefsManager,
) : PlayerFullScreenRepository {

    override val value: Flow<Boolean> = prefs.value.booleanNotNull()

    override suspend fun set(value: Boolean) {
        prefs.save(value)
    }
}
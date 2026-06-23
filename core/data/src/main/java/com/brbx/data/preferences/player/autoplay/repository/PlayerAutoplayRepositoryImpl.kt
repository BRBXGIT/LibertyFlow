package com.brbx.data.preferences.player.autoplay.repository

import com.brbx.data.preferences.common.map.booleanNotNull
import com.brbx.domain.preferences.player.autoplay.repository.PlayerAutoplayRepository
import com.brbx.preferences.player.manager.autoplay.PlayerAutoplayPrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerAutoplayRepositoryImpl(
    private val prefs: PlayerAutoplayPrefsManager,
) : PlayerAutoplayRepository {

    override val value: Flow<Boolean> = prefs.value.booleanNotNull()

    override suspend fun set(value: Boolean) {
        prefs.save(value)
    }
}
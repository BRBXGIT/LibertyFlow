package com.brbx.data.preferences.player.opening.repository

import com.brbx.data.preferences.common.map.booleanNotNull
import com.brbx.domain.preferences.player.opening.repository.PlayerOpeningRepository
import com.brbx.preferences.player.manager.opening.PlayerOpeningPrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerOpeningRepositoryImpl(
    private val prefs: PlayerOpeningPrefsManager,
) : PlayerOpeningRepository {

    override val showSkipButton: Flow<Boolean> = prefs.showSkipButton.booleanNotNull()
    override val autoSkip: Flow<Boolean> = prefs.autoSkip.booleanNotNull()

    override suspend fun setShowSkipButton(showSkipButton: Boolean) {
        prefs.saveShowSkipButton(showSkipButton)
    }

    override suspend fun setAutoSkip(autoSkip: Boolean) {
        prefs.saveAutoSkip(autoSkip)
    }
}
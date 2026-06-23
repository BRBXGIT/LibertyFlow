package com.brbx.domain.preferences.player.opening.repository

import kotlinx.coroutines.flow.Flow

interface PlayerOpeningRepository {

    val showSkipButton: Flow<Boolean>
    val autoSkip: Flow<Boolean>

    suspend fun setShowSkipButton(showSkipButton: Boolean)
    suspend fun setAutoSkip(autoSkip: Boolean)
}
package com.brbx.preferences.player.manager.opening

import kotlinx.coroutines.flow.Flow

interface PlayerOpeningPrefsManager {

    val showSkipButton: Flow<Boolean?>
    val authSkip: Flow<Boolean?>

    suspend fun saveShowSkipButton(showSkipButton: Boolean)
    suspend fun saveAutoSkip(autoSkip: Boolean)
}
package com.brbx.preferences.player.manager.fullscreen

import kotlinx.coroutines.flow.Flow

interface PlayerFullScreenPrefsManager {

    val fullScreen: Flow<Boolean?>

    suspend fun saveFullScreen(fullScreen: Boolean)
}
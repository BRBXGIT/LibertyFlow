package com.brbx.preferences.player.manager.auto_play

import kotlinx.coroutines.flow.Flow

interface PlayerAutoPlayPrefsManager {

    val autoPlay: Flow<Boolean?>

    suspend fun saveAutoPlay(autoPlay: Boolean)
}
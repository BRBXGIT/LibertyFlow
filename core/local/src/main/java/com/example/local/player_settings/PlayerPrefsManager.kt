package com.example.local.player_settings

import kotlinx.coroutines.flow.Flow

interface PlayerPrefsManager {

    val quality: Flow<String?>

    val showSkipOpeningButton: Flow<Boolean?>

    val autoSkipOpening: Flow<Boolean?>

    val autoPlay: Flow<Boolean?>

    val isCropped: Flow<Boolean?>

    suspend fun saveQuality(quality: String)

    suspend fun saveShowSkipOpeningButton(show: Boolean)

    suspend fun saveAutoSkipOpening(skip: Boolean)

    suspend fun saveAutoPlay(autoPlay: Boolean)

    suspend fun saveIsCopped(isCopped: Boolean)
}
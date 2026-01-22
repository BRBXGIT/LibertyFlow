package com.example.data.domain

import com.example.data.models.player.VideoQuality
import kotlinx.coroutines.flow.Flow

interface PlayerSettingsRepo {

    val quality: Flow<VideoQuality>

    val showSkipOpeningButton: Flow<Boolean>

    val autoSkipOpening: Flow<Boolean>

    val autoPlay: Flow<Boolean>

    suspend fun saveQuality(quality: VideoQuality)

    suspend fun saveShowSkipOpeningButton(show: Boolean)

    suspend fun saveAutoSkipOpening(skip: Boolean)

    suspend fun saveAutoPlay(autoPlay: Boolean)
}
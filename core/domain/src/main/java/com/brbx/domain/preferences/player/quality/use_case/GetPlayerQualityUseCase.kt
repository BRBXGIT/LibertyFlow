package com.brbx.domain.preferences.player.quality.use_case

import com.brbx.domain.preferences.player.quality.model.VideoQuality
import com.brbx.domain.preferences.player.quality.repository.PlayerQualityRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerQualityUseCase(
    private val repository: PlayerQualityRepository,
) {
    operator fun invoke(): Flow<VideoQuality> = repository.value
}
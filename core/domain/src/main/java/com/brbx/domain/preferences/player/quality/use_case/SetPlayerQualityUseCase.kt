package com.brbx.domain.preferences.player.quality.use_case

import com.brbx.domain.preferences.player.quality.model.VideoQuality
import com.brbx.domain.preferences.player.quality.repository.PlayerQualityRepository

class SetPlayerQualityUseCase(
    private val repository: PlayerQualityRepository,
) {
    suspend operator fun invoke(videoQuality: VideoQuality) {
        repository.set(videoQuality)
    }
}
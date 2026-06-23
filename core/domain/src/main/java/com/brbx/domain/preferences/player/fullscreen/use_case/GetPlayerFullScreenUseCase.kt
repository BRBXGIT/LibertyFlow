package com.brbx.domain.preferences.player.fullscreen.use_case

import com.brbx.domain.preferences.player.fullscreen.repository.PlayerFullScreenRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerFullScreenUseCase(
    private val repository: PlayerFullScreenRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.value
}
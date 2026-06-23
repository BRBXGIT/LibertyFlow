package com.brbx.domain.preferences.player.autoplay.use_case

import com.brbx.domain.preferences.player.autoplay.repository.PlayerAutoplayRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerAutoplayUseCase(
    private val repository: PlayerAutoplayRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.value
}
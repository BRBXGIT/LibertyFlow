package com.brbx.domain.preferences.player.opening.use_case

import com.brbx.domain.preferences.player.opening.repository.PlayerOpeningRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerAutoSkipOpeningUseCase(
    private val repository: PlayerOpeningRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.autoSkip
}
package com.brbx.domain.preferences.player.opening.use_case

import com.brbx.domain.preferences.player.opening.repository.PlayerOpeningRepository

class SetPlayerAutoSkipOpeningUseCase(
    private val repository: PlayerOpeningRepository,
) {
    suspend operator fun invoke(autoSkip: Boolean) = repository.setAutoSkip(autoSkip)
}
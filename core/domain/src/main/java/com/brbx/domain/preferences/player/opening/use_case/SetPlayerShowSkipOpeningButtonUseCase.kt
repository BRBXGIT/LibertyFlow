package com.brbx.domain.preferences.player.opening.use_case

import com.brbx.domain.preferences.player.opening.repository.PlayerOpeningRepository

class SetPlayerShowSkipOpeningButtonUseCase(
    private val repository: PlayerOpeningRepository,
) {
    suspend operator fun invoke(show: Boolean) {
        repository.setShowSkipButton(show)
    }
}
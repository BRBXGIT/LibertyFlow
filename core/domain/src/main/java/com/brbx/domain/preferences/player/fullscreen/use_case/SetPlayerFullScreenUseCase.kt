package com.brbx.domain.preferences.player.fullscreen.use_case

import com.brbx.domain.preferences.player.fullscreen.repository.PlayerFullScreenRepository

class SetPlayerFullScreenUseCase(
    private val repository: PlayerFullScreenRepository,
) {
    suspend operator fun invoke(fullscreen: Boolean) {
        repository.set(fullscreen)
    }
}
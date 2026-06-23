package com.brbx.domain.preferences.player.autoplay.use_case

import com.brbx.domain.preferences.player.autoplay.repository.PlayerAutoplayRepository

class SetPlayerAutoplayUseCase(
    private val repository: PlayerAutoplayRepository,
) {
    suspend operator fun invoke(autoplay: Boolean) {
        repository.set(autoplay)
    }
}
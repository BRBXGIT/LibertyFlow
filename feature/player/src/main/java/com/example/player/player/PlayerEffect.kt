package com.example.player.player

sealed interface PlayerEffect {
    // --- Player ---
    data object TryPipEnterPip: PlayerEffect
}
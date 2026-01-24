package com.example.player.player

sealed interface PlayerEffect {
    // --- Player ---
    data class SeekForFiveSeconds(val forward: Boolean): PlayerEffect
    data class SkipEpisode(val forward: Boolean): PlayerEffect
    data class ChangeEpisode(val index: Int): PlayerEffect
    data class SeekTo(val position: Long): PlayerEffect
}
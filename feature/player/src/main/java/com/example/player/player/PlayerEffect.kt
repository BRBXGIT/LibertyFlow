package com.example.player.player

import com.example.data.models.releases.anime_details.UiEpisode

sealed interface PlayerEffect {
    // --- Player ---
    data class SetUpPlayer(val episodes: List<UiEpisode>, val startIndex: Int): PlayerEffect
    data class SeekForFiveSeconds(val forward: Boolean): PlayerEffect
    data object TogglePlayPause: PlayerEffect
    data object StopPlayer: PlayerEffect

    // --- Controller ---
    data object ToggleControllerVisible: PlayerEffect

    // --- Ui ---
    data object ToggleFullScreen : PlayerEffect
}
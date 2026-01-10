package com.example.player.player

import com.example.data.models.releases.anime_details.UiEpisode

sealed interface PlayerEffect {
    data class SetUpPlayer(val episodes: List<UiEpisode>, val startIndex: Int): PlayerEffect
}
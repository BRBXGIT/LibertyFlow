@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState
import kotlinx.coroutines.delay

@Composable
internal fun FullScreenPlayerContainer(
    player: ExoPlayer,
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
) {
    BackHandler {
        onPlayerEffect(PlayerEffect.ToggleFullScreen)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Player(player = player)
    }
}
@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.design_system.theme.LibertyFlowIcons
import com.example.player.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState

@Composable
fun FullScreenPlayer(
    player: ExoPlayer,
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sharedElement(
                    rememberSharedContentState(key = "player_video"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .background(Color.Black)
        ) {
            Player(player)

            // TODO: Full controller

            // Кнопка свернуть обратно
            IconButton(
                onClick = { onPlayerEffect(PlayerEffect.ToggleFullScreen) },
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(painter = painterResource(LibertyFlowIcons.ArrowLeftFilled), contentDescription = null, tint = Color.White)
            }
        }
    }
}
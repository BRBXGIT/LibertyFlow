@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components

import android.view.Window
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState

@Composable
fun FullScreenPlayerContainer(
    player: ExoPlayer,
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    BackHandler {
        onPlayerEffect(PlayerEffect.ToggleFullScreen)
    }

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = PLAYER_SHARED_ELEMENT_KEY),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .background(Color.Black)
        ) {
            Player(player = player, isLandscape = true)
        }
    }
}
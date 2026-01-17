package com.example.player.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import com.example.player.player.PlayerState

private const val zIndex = 0f

@Composable
internal fun BoxScope.Player(player: ExoPlayer, playerState: PlayerState) {
    PlayerSurface(
        surfaceType = if (playerState.uiPlayerState == PlayerState.UiPlayerState.Full) {
            SURFACE_TYPE_SURFACE_VIEW
        } else {
            SURFACE_TYPE_TEXTURE_VIEW
        },
        player = player,
        modifier = Modifier
            .align(Alignment.Center)
            .zIndex(zIndex)
            .fillMaxSize()
    )
}
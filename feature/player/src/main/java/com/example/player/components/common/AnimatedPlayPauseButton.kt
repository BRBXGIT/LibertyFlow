package com.example.player.components.common

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import com.example.design_system.theme.LibertyFlowIcons
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

@Composable
internal fun AnimatedPlayPauseButton(
    playerState: PlayerState,
    onPlayerIntent: (PlayerIntent) -> Unit,
    iconSize: Dp,
    buttonSize: Dp,
    isEnabled: Boolean = true
) {
    // Animated Play/Pause icon handling
    val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.PlayPauseAnimated)
    val isPaused = playerState.episodeState == PlayerState.EpisodeState.Paused
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = animatedVector,
        atEnd = !isPaused
    )

    IconButton(
        modifier = Modifier.size(buttonSize),
        onClick = { if (playerState.isControllerVisible) onPlayerIntent(PlayerIntent.TogglePlayPause) },
        enabled = isEnabled
    ) {
        Image(
            contentDescription = null,
            painter = painter,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(iconSize)
        )
    }
}
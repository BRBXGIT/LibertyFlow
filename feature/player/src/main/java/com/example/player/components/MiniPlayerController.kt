@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState

private const val VISIBLE = 1f
private const val INVISIBLE = 0f
private const val ALMOST_INVISIBLE = 0.7f

private const val CONTROLLER_PADDING = 4
private const val ANIMATED_CONTROLLER_ALPHA_LABEL = "Animated alpha for controller"
private const val ANIMATED_CONTROLLER_TINT_ALPHA = "Animated alpha for controller tint"

@Composable
internal fun BoxScope.MiniPlayerController(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    val visible = playerState.isControllerVisible

    // Animates the overall visibility of the controller overlay
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) VISIBLE else INVISIBLE,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = ANIMATED_CONTROLLER_ALPHA_LABEL
    )

    // Animates the background dimming effect (tint)
    val animatedTintAlpha by animateFloatAsState(
        targetValue = if (visible) ALMOST_INVISIBLE else INVISIBLE,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = ANIMATED_CONTROLLER_TINT_ALPHA
    )

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize()
            .zIndex(1f) // Ensure controller stays above the content
            .graphicsLayer { alpha = animatedAlpha }
            .clip(mShapes.small)
            .background(Color.Black.copy(alpha = animatedTintAlpha))
            .clickable(
                enabled = true,
                onClick = { onPlayerEffect(PlayerEffect.ToggleControllerVisible) }
            )
            .padding(CONTROLLER_PADDING.dp)
    ) {
        ControllerButton(
            icon = LibertyFlowIcons.FullScreen,
            onClick = { /* TODO: Handle click */ },
            visible = visible,
            modifier = Modifier.align(Alignment.TopStart)
        )

        // Top-right exit/stop button
        ControllerButton(
            icon = LibertyFlowIcons.CrossCircle,
            onClick = { onPlayerEffect(PlayerEffect.StopPlayer) },
            visible = visible,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        // Bottom control row (Rewind - Play/Pause - Fast Forward)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            ControllerButton(
                icon = LibertyFlowIcons.RewindBack,
                onClick = { onPlayerEffect(PlayerEffect.SeekForFiveSeconds(false)) },
                visible = visible
            )

            // Animated Play/Pause icon handling
            val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.PlayPauseAnimated)
            val isPaused = playerState.episodeState == PlayerState.EpisodeState.Paused
            val painter = rememberAnimatedVectorPainter(
                animatedImageVector = animatedVector,
                atEnd = !isPaused
            )

            IconButton(
                onClick = { if (visible) onPlayerEffect(PlayerEffect.TogglePlayPause) }
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Play/Pause Toggle",
                    colorFilter = ColorFilter.tint(mColors.onBackground),
                    modifier = Modifier.size(ICON_SIZE.dp)
                )
            }

            ControllerButton(
                icon = LibertyFlowIcons.Rewind,
                onClick = { onPlayerEffect(PlayerEffect.SeekForFiveSeconds(true)) },
                visible = visible
            )
        }
    }
}

private const val ICON_SIZE = 24

/**
 * A reusable icon button for player controls.
 * Uses a conditional click action based on visibility to prevent accidental triggers.
 */
@Composable
private fun ControllerButton(
    icon: Int,
    onClick: () -> Unit,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { if (visible) onClick() },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(ICON_SIZE.dp),
            tint = mColors.onBackground
        )
    }
}
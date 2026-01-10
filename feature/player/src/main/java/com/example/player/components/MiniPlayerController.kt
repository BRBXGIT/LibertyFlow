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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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

    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) VISIBLE else INVISIBLE,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = ANIMATED_CONTROLLER_ALPHA_LABEL
    )
    val animatedTintAlpha by animateFloatAsState(
        targetValue = if (visible) ALMOST_INVISIBLE else INVISIBLE,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = ANIMATED_CONTROLLER_TINT_ALPHA
    )

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .clip(mShapes.small)
            .background(
                color = if (visible) Color.Black.copy(alpha = animatedTintAlpha) else Color.Transparent
            )
            .clickable { onPlayerEffect(PlayerEffect.ToggleControllerVisible) }
            .fillMaxSize()
            .alpha(animatedAlpha)
            .zIndex(1f)
            .padding(CONTROLLER_PADDING.dp)
    ) {
        ControllerButton(
            onClick = { onPlayerEffect(PlayerEffect.StopPlayer) },
            icon = LibertyFlowIcons.CrossCircle,
            modifier = Modifier.align(Alignment.TopEnd),
            visible = visible
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            ControllerButton(
                onClick = { onPlayerEffect(PlayerEffect.SeekForFiveSeconds(false)) },
                icon = LibertyFlowIcons.RewindBack,
                visible = visible
            )

            val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.PlayPauseAnimated)
            val painter = rememberAnimatedVectorPainter(
                animatedImageVector = animatedVector,
                atEnd = playerState.episodeState != PlayerState.EpisodeState.Paused
            )
            IconButton(
                onClick = { if (visible) onPlayerEffect(PlayerEffect.TogglePlayPause) }
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(mColors.onBackground),
                    modifier = Modifier.size(ICON_SIZE.dp)
                )
            }

            ControllerButton(
                onClick = { onPlayerEffect(PlayerEffect.SeekForFiveSeconds(true)) },
                icon = LibertyFlowIcons.Rewind,
                visible = visible
            )
        }
    }
}

private const val ICON_SIZE = 24

@Composable
private fun ControllerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Int,
    visible: Boolean
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
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.player.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val WIDTH = 200
private const val HEIGHT = 120
private const val MARGIN = 16
private const val CENTER_DIVIDER = 2

private const val ZERO_OFFSET = 0f
private const val VISIBLE = 1f
private const val INVISIBLE = 0f
private const val ALPHA_ANIMATION_LABEL = "Animate player visible"

@Composable
fun PlayerContainer(
    navBarVisible: Boolean,
    player: ExoPlayer,
    playerState: PlayerState,
    playerEffects: Flow<PlayerEffect>,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val playerWidthPx = with(density) { WIDTH.dp.toPx() }
    val playerHeightPx = with(density) { HEIGHT.dp.toPx() }
    val marginPx = with(density) { MARGIN.dp.toPx() }

    // Calculate actual bottom margin including Nav Bar
    val navBarSize = calculateNavBarSize()
    val bottomMarginPx = with(density) {
        MARGIN.dp.toPx() + if (navBarVisible) navBarSize.toPx() else ZERO_OFFSET
    }

    // Calculate actual top margin
    val statusBarHeightPx = with(density) { calculateStatusBarSize().toPx() }
    val topLimitPx = statusBarHeightPx + marginPx

    BoxWithConstraints {
        val screenWidthPx = constraints.maxWidth.toFloat()
        val screenHeightPx = constraints.maxHeight.toFloat()

        // 1. Initial position (only set once)
        val offset = remember {
            Animatable(
                initialValue = Offset(
                    x = screenWidthPx - playerWidthPx - marginPx,
                    y = screenHeightPx - playerHeightPx - bottomMarginPx
                ),
                typeConverter = Offset.VectorConverter
            )
        }

        // 2. React to Navigation Bar visibility changes
        // This will smoothly move the player up/down when navBar appears
        LaunchedEffect(navBarVisible) {
            val currentX = offset.value.x
            // Check if player is currently snapped to the bottom
            val isAtBottom = offset.value.y > (screenHeightPx / CENTER_DIVIDER)

            if (isAtBottom) {
                val targetY = screenHeightPx - playerHeightPx - bottomMarginPx
                offset.animateTo(Offset(currentX, targetY))
            }
        }

        val motionScheme = mMotionScheme
        val animatedPlayerAlpha by animateFloatAsState(
            targetValue = if (playerState.playerState != PlayerState.PlayerState.Closed) VISIBLE else INVISIBLE,
            animationSpec = motionScheme.slowEffectsSpec(),
            label = ALPHA_ANIMATION_LABEL
        )
        Box(
            modifier = Modifier
                .alpha(animatedPlayerAlpha)
                .offset { IntOffset(x = offset.value.x.roundToInt(), y = offset.value.y.roundToInt()) }
                .size(WIDTH.dp, HEIGHT.dp)
                .clip(mShapes.small)
                .background(Color.Black)
                .pointerInput(navBarVisible) { // Re-bind input when navBar changes
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()

                            val limitYMax = screenHeightPx - playerHeightPx - bottomMarginPx

                            val newX = (offset.value.x + dragAmount.x)
                                .coerceIn(marginPx, screenWidthPx - playerWidthPx - marginPx)
                            val newY = (offset.value.y + dragAmount.y)
                                .coerceIn(topLimitPx, limitYMax)

                            scope.launch { offset.snapTo(Offset(newX, newY)) }
                        },
                        onDragEnd = {
                            val centerX = offset.value.x + playerWidthPx / CENTER_DIVIDER
                            val centerY = offset.value.y + playerHeightPx / CENTER_DIVIDER

                            val targetX = if (centerX < screenWidthPx / CENTER_DIVIDER) marginPx
                            else screenWidthPx - playerWidthPx - marginPx

                            val targetY = if (centerY < screenHeightPx / CENTER_DIVIDER) {
                                topLimitPx
                            } else {
                                screenHeightPx - playerHeightPx - bottomMarginPx
                            }

                            scope.launch {
                                offset.animateTo(
                                    targetValue = Offset(targetX, targetY),
                                    animationSpec = motionScheme.slowSpatialSpec()
                                )
                            }
                        }
                    )
                }
        ) {
            MiniPlayerController(playerState, onPlayerEffect)

            Player(player)
        }
    }
}

@Composable
private fun calculateStatusBarSize(): Dp {
    return WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
}
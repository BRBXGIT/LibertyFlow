@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
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


/**
 * A floating player container that supports dragging and snapping to screen corners.
 * Adjusts its position dynamically based on Navigation Bar visibility.
 */
@Composable
fun PlayerContainer(
    navBarVisible: Boolean,
    player: ExoPlayer,
    playerState: PlayerState,
    playerEffects: Flow<PlayerEffect>,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    AnimatedVisibility(
        visible = playerState.playerState != PlayerState.PlayerState.Closed,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        val density = LocalDensity.current
        val scope = rememberCoroutineScope()

        // Convert DP constants to pixels for precise coordinate calculations
        val playerWidthPx = with(density) { WIDTH.dp.toPx() }
        val playerHeightPx = with(density) { HEIGHT.dp.toPx() }
        val marginPx = with(density) { MARGIN.dp.toPx() }

        // Dynamic calculation of available screen space considering system bars
        val navBarSize = calculateNavBarSize()
        val bottomMarginPx = with(density) {
            MARGIN.dp.toPx() + if (navBarVisible) navBarSize.toPx() else ZERO_OFFSET
        }
        val statusBarHeightPx = with(density) { calculateStatusBarSize().toPx() }
        val topLimitPx = statusBarHeightPx + marginPx

        BoxWithConstraints {
            val screenWidthPx = constraints.maxWidth.toFloat()
            val screenHeightPx = constraints.maxHeight.toFloat()

            // Persistent state for the player's position
            val offset = remember {
                Animatable(
                    initialValue = Offset(
                        x = screenWidthPx - playerWidthPx - marginPx,
                        y = screenHeightPx - playerHeightPx - bottomMarginPx
                    ),
                    typeConverter = Offset.VectorConverter
                )
            }

            // Adjust vertical position when Navigation Bar appears/disappears
            LaunchedEffect(navBarVisible) {
                val isAtBottomHalf = offset.value.y > (screenHeightPx / CENTER_DIVIDER)
                if (isAtBottomHalf) {
                    val targetY = screenHeightPx - playerHeightPx - bottomMarginPx
                    offset.animateTo(Offset(offset.value.x, targetY))
                }
            }

            val motionScheme = mMotionScheme
            Box(
                modifier = Modifier
                    // Use lambda-based offset to avoid frequent recompositions during drag
                    .offset {
                        IntOffset(
                            offset.value.x.roundToInt(),
                            offset.value.y.roundToInt()
                        )
                    }
                    .size(WIDTH.dp, HEIGHT.dp)
                    .clip(mShapes.small)
                    .background(Color.Black)
                    .pointerInput(navBarVisible) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()

                                // Define drag boundaries
                                val limitYMax = screenHeightPx - playerHeightPx - bottomMarginPx
                                val newX = (offset.value.x + dragAmount.x)
                                    .coerceIn(marginPx, screenWidthPx - playerWidthPx - marginPx)
                                val newY = (offset.value.y + dragAmount.y)
                                    .coerceIn(topLimitPx, limitYMax)

                                scope.launch { offset.snapTo(Offset(newX, newY)) }
                            },
                            onDragEnd = {
                                // Snap logic: find the nearest horizontal and vertical edge
                                val centerX = offset.value.x + playerWidthPx / CENTER_DIVIDER
                                val centerY = offset.value.y + playerHeightPx / CENTER_DIVIDER

                                val targetX = if (centerX < screenWidthPx / CENTER_DIVIDER) {
                                    marginPx
                                } else {
                                    screenWidthPx - playerWidthPx - marginPx
                                }

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
                // Internal UI layers
                MiniPlayerController(playerState, onPlayerEffect)
                Player(player)
            }
        }
    }
}

/**
 * Utility to retrieve current status bar height for boundary calculations.
 */
@Composable
private fun calculateStatusBarSize() =
    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

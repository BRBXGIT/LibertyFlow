@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class)

package com.example.player.components.mini

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MotionScheme
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.player.components.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val WIDTH = 200
private const val HEIGHT = 120
private const val MARGIN = 16
private const val CENTER_DIVIDER = 2
private const val ZERO = 0

@Composable
internal fun MiniPlayerContainer(
    navBarVisible: Boolean,
    player: ExoPlayer,
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val density = LocalDensity.current
    val navBarSize = calculateNavBarSize()
    val statusBarSize = calculateStatusBarSize()

    val constraints = remember(density, navBarVisible, navBarSize, statusBarSize) {
        PlayerConstraints(density, navBarVisible, navBarSize, statusBarSize)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val playerStateHolder = rememberMiniPlayerState(
            screenWidth = constraints.toPx(maxWidth),
            screenHeight = constraints.toPx(maxHeight),
            playerConstraints = constraints
        )

        LaunchedEffect(navBarVisible) {
            playerStateHolder.onNavBarVisibilityChanged()
        }

        Box(
            modifier = Modifier
                .offset { playerStateHolder.offset.value.toIntOffset() }
                .size(WIDTH.dp, HEIGHT.dp)
                .clip(mShapes.small)
                .background(Color.Black)
                .pointerInput(navBarVisible) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            playerStateHolder.dragBy(dragAmount)
                        },
                        onDragEnd = { playerStateHolder.snapToCorner() }
                    )
                }
        ) {
            MiniPlayerController(playerState, onPlayerEffect, onPlayerIntent)
            Player(player, playerState)
        }
    }
}

/**
 * Size and paddings in px
 */
private class PlayerConstraints(
    private val density: Density,
    val navBarVisible: Boolean,
    val navBarSize: Dp,
    val statusBarSize: Dp
) {
    val widthPx = with(density) { WIDTH.dp.toPx() }
    val heightPx = with(density) { HEIGHT.dp.toPx() }
    val marginPx = with(density) { MARGIN.dp.toPx() }
    val topLimitPx = with(density) { (statusBarSize + MARGIN.dp).toPx() }
    val bottomMarginPx = with(density) {
        (MARGIN.dp + if (navBarVisible) navBarSize else ZERO.dp).toPx()
    }

    fun toPx(dp: Dp) = with(density) { dp.toPx() }
}

/**
 * Position and animation states
 */
@Composable
private fun rememberMiniPlayerState(
    screenWidth: Float,
    screenHeight: Float,
    playerConstraints: PlayerConstraints
): MiniPlayerStateHolder {
    val scope = rememberCoroutineScope()
    val motionScheme = mMotionScheme

    return remember(screenWidth, screenHeight, playerConstraints) {
        MiniPlayerStateHolder(scope, screenWidth, screenHeight, playerConstraints, motionScheme)
    }
}

private class MiniPlayerStateHolder(
    private val scope: CoroutineScope,
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val config: PlayerConstraints,
    private val motionScheme: MotionScheme
) {
    val offset = Animatable(
        initialValue = Offset(
            x = screenWidth - config.widthPx - config.marginPx,
            y = screenHeight - config.heightPx - config.bottomMarginPx
        ),
        typeConverter = Offset.VectorConverter
    )

    private val limitYMax get() = screenHeight - config.heightPx - config.bottomMarginPx
    private val limitXMax get() = screenWidth - config.widthPx - config.marginPx

    fun dragBy(dragAmount: Offset) {
        val newX = (offset.value.x + dragAmount.x).coerceIn(config.marginPx, limitXMax)
        val newY = (offset.value.y + dragAmount.y).coerceIn(config.topLimitPx, limitYMax)
        scope.launch { offset.snapTo(Offset(newX, newY)) }
    }

    fun snapToCorner() {
        val centerX = offset.value.x + config.widthPx / CENTER_DIVIDER
        val centerY = offset.value.y + config.heightPx / CENTER_DIVIDER

        val targetX = if (centerX < screenWidth / CENTER_DIVIDER) config.marginPx else limitXMax
        val targetY = if (centerY < screenHeight / CENTER_DIVIDER) config.topLimitPx else limitYMax

        scope.launch {
            offset.animateTo(
                targetValue = Offset(targetX, targetY),
                animationSpec = motionScheme.slowSpatialSpec()
            )
        }
    }

    suspend fun onNavBarVisibilityChanged() {
        val isAtBottomHalf = offset.value.y > (screenHeight / CENTER_DIVIDER)
        if (isAtBottomHalf) {
            offset.animateTo(Offset(offset.value.x, limitYMax))
        }
    }
}

// Extension
private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

/**
 * Utility to retrieve current status bar height for boundary calculations.
 */
@Composable
private fun calculateStatusBarSize() =
    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
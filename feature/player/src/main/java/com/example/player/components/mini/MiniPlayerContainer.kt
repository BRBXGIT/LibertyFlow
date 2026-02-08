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
import androidx.compose.runtime.SideEffect
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
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes
import com.example.player.components.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// UI Constants for the mini player dimensions and spacing
private const val WIDTH = 200
private const val HEIGHT = 120
private const val MARGIN = 16
private const val CENTER_DIVIDER = 2
private const val ZERO = 0

/**
 * The main container for the Mini Player.
 * Handles the layout, gesture detection, and coordination between player state and UI.
 */
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

    // Memoize constraints to avoid recalculation unless layout factors change
    val constraints = remember(density, navBarVisible, navBarSize, statusBarSize) {
        PlayerConstraints(density, navBarVisible, navBarSize, statusBarSize)
    }

    // BoxWithConstraints provides the maxWidth/maxHeight of the parent container
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        // Initialize the state holder that manages position and animation logic
        val playerStateHolder = rememberMiniPlayerState(
            screenWidth = constraints.toPx(maxWidth),
            screenHeight = constraints.toPx(maxHeight),
            playerConstraints = constraints
        )

        // React to navigation bar visibility changes (e.g., adjust player position)
        LaunchedEffect(navBarVisible) {
            playerStateHolder.onNavBarVisibilityChanged()
        }

        Box(
            modifier = Modifier
                .offset { playerStateHolder.offset.value.toIntOffset() } // Apply dynamic position
                .size(WIDTH.dp, HEIGHT.dp)
                .clip(mShapes.small)
                .background(Color.Black)
                .pointerInput(navBarVisible) {
                    // Handle drag gestures
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume() // Prevent parent from scrolling
                            playerStateHolder.dragBy(dragAmount)
                        },
                        onDragEnd = {
                            // When released, snap the player to the nearest screen corner
                            playerStateHolder.snapToCorner()
                        }
                    )
                }
        ) {
            // Render the control overlay and the actual Video surface
            MiniPlayerController(playerState, onPlayerEffect, onPlayerIntent)
            Player(player, playerState)
        }
    }
}

/**
 * Helper class to calculate and store boundaries in Pixels.
 * Considers system bars (Status/Navigation) and margins.
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

    // Top boundary: Status bar height + defined margin
    val topLimitPx = with(density) { (statusBarSize + MARGIN.dp).toPx() }

    // Bottom boundary: Accounts for navigation bar visibility
    val bottomMarginPx = with(density) {
        (MARGIN.dp + if (navBarVisible) navBarSize else ZERO.dp).toPx()
    }

    // Utility function for DP to PX conversion
    fun toPx(dp: Dp) = with(density) { dp.toPx() }
}

/**
 * Factory function to create and remember the MiniPlayerStateHolder.
 */
@Composable
private fun rememberMiniPlayerState(
    screenWidth: Float,
    screenHeight: Float,
    playerConstraints: PlayerConstraints
): MiniPlayerStateHolder {
    val scope = rememberCoroutineScope()
    val motionScheme = mMotionScheme

    // Recreate holder only if screen dimensions change
    val holder = remember(screenWidth, screenHeight) {
        MiniPlayerStateHolder(scope, screenWidth, screenHeight, playerConstraints, motionScheme)
    }

    // Ensure the holder always has the latest layout constraints
    SideEffect {
        holder.updateConfig(playerConstraints)
    }

    return holder
}

/**
 * State holder that manages the physics and movement of the mini player.
 */
private class MiniPlayerStateHolder(
    private val scope: CoroutineScope,
    private val screenWidth: Float,
    private val screenHeight: Float,
    private var config: PlayerConstraints,
    private val motionScheme: MotionScheme
) {
    // The animatable offset representing the current (x, y) position
    val offset = Animatable(
        initialValue = Offset(
            x = screenWidth - config.widthPx - config.marginPx, // Initial: Bottom Right
            y = screenHeight - config.heightPx - config.bottomMarginPx
        ),
        typeConverter = Offset.VectorConverter
    )

    // Calculated maximum bounds for X and Y axes
    private val limitYMax get() = screenHeight - config.heightPx - config.bottomMarginPx
    private val limitXMax get() = screenWidth - config.widthPx - config.marginPx

    fun updateConfig(newConfig: PlayerConstraints) {
        config = newConfig
    }

    /**
     * Updates the position during a drag event, keeping the player within screen bounds.
     */
    fun dragBy(dragAmount: Offset) {
        val newX = (offset.value.x + dragAmount.x).coerceIn(config.marginPx, limitXMax)
        val newY = (offset.value.y + dragAmount.y).coerceIn(config.topLimitPx, limitYMax)
        scope.launch { offset.snapTo(Offset(newX, newY)) }
    }

    /**
     * Animates the player to the nearest corner of the screen when released.
     */
    fun snapToCorner() {
        val centerX = offset.value.x + config.widthPx / CENTER_DIVIDER
        val centerY = offset.value.y + config.heightPx / CENTER_DIVIDER

        // Determine target corner based on which quadrant the center point is in
        val targetX = if (centerX < screenWidth / CENTER_DIVIDER) config.marginPx else limitXMax
        val targetY = if (centerY < screenHeight / CENTER_DIVIDER) config.topLimitPx else limitYMax

        scope.launch {
            offset.animateTo(
                targetValue = Offset(targetX, targetY),
                animationSpec = motionScheme.slowSpatialSpec()
            )
        }
    }

    /**
     * Smoothly adjusts the player position if the navigation bar appears/disappears.
     * Only triggers if the player is currently in the bottom half of the screen.
     */
    suspend fun onNavBarVisibilityChanged() {
        val isInBottomHalf = offset.value.y > (screenHeight / CENTER_DIVIDER)

        if (isInBottomHalf) {
            offset.animateTo(
                targetValue = offset.value.copy(y = limitYMax),
                animationSpec = motionScheme.slowSpatialSpec()
            )
        }
    }
}

// Extension to convert Float Offset to IntOffset for the Modifier.offset call
private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

/**
 * Utility to retrieve current status bar height for boundary calculations.
 */
@Composable
private fun calculateStatusBarSize() =
    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
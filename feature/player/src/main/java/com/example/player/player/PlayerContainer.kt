@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val WIDTH = 200
private const val HEIGHT = 120
private const val MARGIN = 16
private const val CENTER_DIVIDER = 2

private const val ZERO_OFFSET = 0f

@Composable
fun PlayerContainer(navBarVisible: Boolean) {
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
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
                .size(WIDTH.dp, HEIGHT.dp)
                .clip(mShapes.small)
                .background(Color.Black)
                .pointerInput(navBarVisible) { // Re-bind input when navBar changes
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()

                            // Use dynamic bottom limit instead of 0f..screenHeight
                            val limitY = screenHeightPx - playerHeightPx - bottomMarginPx

                            val newX = (offset.value.x + dragAmount.x)
                                .coerceIn(marginPx, screenWidthPx - playerWidthPx - marginPx)
                            val newY = (offset.value.y + dragAmount.y)
                                .coerceIn(marginPx, limitY)

                            scope.launch {
                                offset.snapTo(Offset(newX, newY))
                            }
                        },
                        onDragEnd = {
                            val centerX = offset.value.x + playerWidthPx / CENTER_DIVIDER
                            val centerY = offset.value.y + playerHeightPx / CENTER_DIVIDER

                            val targetX = if (centerX < screenWidthPx / CENTER_DIVIDER) {
                                marginPx
                            } else {
                                screenWidthPx - playerWidthPx - marginPx
                            }

                            val targetY = if (centerY < screenHeightPx / CENTER_DIVIDER) {
                                marginPx
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
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Player Placeholder", color = Color.White)
        }
    }
}
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.containers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.design_system.components.indicators.LibertyFlowLinearIndicator
import com.example.design_system.theme.theme.mMotionScheme

private const val MAX_TRANSLATION_DP = 10f
private const val REFRESH_THRESHOLD = 1.001f
private const val RESET_THRESHOLD = 0.5f
private const val INDICATOR_MAX_SIZE_DP = 42f
private const val VIBRATION_DURATION_MS = 50L
private const val MIN_PROGRESS = 0f
private const val MAX_PROGRESS = 1f

/**
 * A container that provides pull-to-refresh functionality with a custom vibrating feedback
 * and dynamic loading indicators.
 *
 * @param isSearching Determines which type of indicator to show (Search vs General).
 * @param isRefreshing Whether the refresh action is currently in progress.
 * @param onRefresh Callback triggered when the refresh threshold is met.
 * @param modifier Modifier to be applied to the layout.
 * @param content The scrollable content within the pull-to-refresh container.
 */
@Composable
fun VibratingContainer(
    isSearching: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()

    // Handle haptic feedback based on pull distance
    VibrationHandler(state.distanceFraction)

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        indicator = {}, // Custom indicators are handled inside the Column
        modifier = modifier
    ) {
        val translationAnimation by animateDpAsState(
            targetValue = (state.distanceFraction * MAX_TRANSLATION_DP).dp,
            animationSpec = mMotionScheme.fastSpatialSpec(),
            label = "PullTranslationAnimation"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                translationY = translationAnimation.toPx()
            }
        ) {
            RefreshIndicatorArea(
                isSearching = isSearching,
                isRefreshing = isRefreshing,
                distanceFraction = state.distanceFraction
            )
            content()
        }
    }
}

/**
 * Renders the appropriate indicator based on search state and pull progress.
 */
@Composable
private fun RefreshIndicatorArea(
    isSearching: Boolean,
    isRefreshing: Boolean,
    distanceFraction: Float
) {
    when {
        isSearching && isRefreshing -> {
            LibertyFlowLinearIndicator()
        }
        !isSearching && isRefreshing -> {
            ContainedLoadingIndicator(
                modifier = Modifier.size(INDICATOR_MAX_SIZE_DP.dp)
            )
        }
        !isSearching -> {
            val progress = distanceFraction.coerceIn(MIN_PROGRESS, MAX_PROGRESS)
            val dynamicSize = (distanceFraction * INDICATOR_MAX_SIZE_DP)
                .coerceIn(0f, INDICATOR_MAX_SIZE_DP).dp

            ContainedLoadingIndicator(
                progress = { progress },
                modifier = Modifier.size(dynamicSize)
            )
        }
        // If isSearching is true but not refreshing, we show nothing or a specific state
    }
}

/**
 * Manages vibration triggers based on the pull distance.
 * Uses [LaunchedEffect] to ensure vibration logic isn't blocked or over-triggered by recompositions.
 */
@Composable
private fun VibrationHandler(distance: Float) {
    val context = LocalContext.current
    val vibrator = remember(context) { context.getVibrator() }
    var hasVibratedByThreshold by remember { mutableStateOf(false) }

    LaunchedEffect(distance) {
        if (distance >= REFRESH_THRESHOLD && !hasVibratedByThreshold) {
            vibrator?.vibrateOnce(VIBRATION_DURATION_MS)
            hasVibratedByThreshold = true
        } else if (distance < RESET_THRESHOLD && hasVibratedByThreshold) {
            hasVibratedByThreshold = false
        }
    }
}

/**
 * Compatibility-aware vibrator retrieval.
 */
private fun Context.getVibrator(): Vibrator? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
}

/**
 * Triggers a one-shot vibration effect.
 */
private fun Vibrator.vibrateOnce(duration: Long) {
    val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrate(effect)
}
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.containers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private const val MaxTranslationDp = 10f
private const val RefreshThreshold = 1.001f
private const val ResetThreshold = 0.5f
private const val IndicatorMaxSizeDp = 47f
private const val VibrationDurationMs = 50L

@Composable
fun VibratingContainer(
    isSearching: Boolean,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        indicator = {},
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                translationY = state.distanceFraction * MaxTranslationDp.dp.toPx()
            }
        ) {
            if (!isSearching) {
                CreateVibration(state.distanceFraction)

                if (isRefreshing) {
                    ContainedLoadingIndicator(
                        modifier = Modifier.size(IndicatorMaxSizeDp.dp)
                    )
                } else {
                    val progress = state.distanceFraction.coerceIn(0f, 1f)
                    val size = (state.distanceFraction * IndicatorMaxSizeDp)
                        .coerceIn(0f, IndicatorMaxSizeDp).dp

                    ContainedLoadingIndicator(
                        progress = { progress },
                        modifier = Modifier.size(size)
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun CreateVibration(distance: Float) {
    val context = LocalContext.current
    val vibrator = remember(context) { context.getVibrator() }
    var didVibrate by remember { mutableStateOf(false) }

    SideEffect {
        if (distance < ResetThreshold && didVibrate) {
            didVibrate = false
        } else if (distance >= RefreshThreshold && !didVibrate) {
            didVibrate = true
            vibrator?.vibrateOnce(VibrationDurationMs)
        }
    }
}

/**
 * Optimized vibrator retrieval with API compatibility check.
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

@Suppress("DEPRECATION")
private fun Vibrator.vibrateOnce(duration: Long = 50L) {
    val effect = VibrationEffect.createOneShot(
        duration,
        VibrationEffect.DEFAULT_AMPLITUDE
    )
    vibrate(effect)
}
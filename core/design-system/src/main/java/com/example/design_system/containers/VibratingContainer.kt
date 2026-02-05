@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.containers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun VibratingContainer(
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
        // Optimization: Use graphicsLayer or offset lambda to avoid
        // recomposing the whole Column when distance changes.
        Column(
            modifier = Modifier.graphicsLayer {
                // Reading state inside a lambda delays execution
                // until the Placement/Layer phase.
                translationY = state.distanceFraction * 8.dp.toPx()
            }
        ) {
            // Trigger haptic logic - it still needs to track distance,
            // but now it doesn't affect the UI hierarchy.
            CreateVibration(state.distanceFraction)

            content()
        }
    }
}

@Composable
private fun CreateVibration(distance: Float) {
    val context = LocalContext.current
    // Use remember with context to avoid re-fetching the service
    val vibrator = remember(context) { context.getVibrator() }

    // didVibrate should be a simple remember if it's strictly for
    // the current touch gesture interaction.
    var didVibrate by remember { mutableStateOf(false) }

    // Side effect to handle haptics without triggering UI updates
    SideEffect {
        if (distance < 1f && didVibrate) {
            didVibrate = false
        } else if (distance >= 1.5f && !didVibrate) {
            didVibrate = true
            vibrator?.vibrateOnce()
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
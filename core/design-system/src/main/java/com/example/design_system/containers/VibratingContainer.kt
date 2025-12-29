@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.containers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme

@Composable
fun VibratingContainer(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    // Pull-to-refresh internal state
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        indicator = {}, // Indicator is fully custom (Spacer + vibration)
        modifier = modifier
    ) {
        // Current pull distance (0f..2f)
        val distance = state.distanceFraction

        Column {
            // Animated top padding that increases as user pulls down
            val animatedPullToRefresh by animateDpAsState(
                targetValue = (distance * 8).dp,
                animationSpec = mMotionScheme.fastSpatialSpec(),
                label = "pullToRefreshOffset"
            )

            // Spacer visually pushes content down (the "stretching" effect)
            Spacer(Modifier.height(animatedPullToRefresh))

            // Trigger haptic feedback when user pulls far enough
            CreateVibration(distance)

            // Actual screen content
            content()
        }
    }
}

@Composable
private fun CreateVibration(distance: Float) {
    // Access to system vibrator
    val context = LocalContext.current
    val vibrator = remember(context) { context.getVibrator() }

    // Tracks whether vibration already happened for this pull
    var didVibrate by rememberSaveable { mutableStateOf(false) }

    // Trigger vibration only once when distance exceeds threshold
    LaunchedEffect(distance) {
        when {
            // Reset vibration if user moved back down
            distance < 1f && didVibrate -> {
                didVibrate = false
            }

            // Trigger once when passing vibration threshold
            distance >= 1.5f && !didVibrate -> {
                didVibrate = true
                vibrator?.vibrateOnce()
            }
        }
    }
}

@Suppress("DEPRECATION")
private fun Context.getVibrator(): Vibrator? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
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
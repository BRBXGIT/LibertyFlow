@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.indicators

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.design_system.theme.theme.mDimens

/**
 * A branded, full-width progress indicator that displays a "wavy" animation.
 *
 * This component wraps the Material 3 [LinearWavyProgressIndicator] to ensure
 * consistent horizontal padding and behavior across the app. It is ideal for
 * indeterminate loading states where a more playful or modern visual style is
 * preferred over a standard linear bar.
 *
 * @see LinearWavyProgressIndicator
 */
@Composable
fun LibertyFlowLinearIndicator() {
    LinearWavyProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mDimens.paddingMedium)
    )
}
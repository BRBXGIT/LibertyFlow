@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

private val TabBoxPadding = 8.dp
private val TabTextPadding = 8.dp

private const val ANIMATED_BG_COLOR_LABEL = "Animated tab bg color"
private const val ANIMATED_CONTENT_COLOR_LABEL = "Animated tab bg color"

@Composable
internal fun TabletTab(
    modifier: Modifier = Modifier,
    shape: Shape = mShapes.extraLarge,
    selected: Boolean,
    onClick: () -> Unit,
    text: String
) {
    val animatedBGColor by animateColorAsState(
        targetValue = if (selected) mColors.primaryContainer else Color.Transparent,
        label = ANIMATED_BG_COLOR_LABEL,
        animationSpec = mMotionScheme.fastEffectsSpec()
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (selected) mColors.onPrimaryContainer else mColors.onBackground,
        label = ANIMATED_CONTENT_COLOR_LABEL,
        animationSpec = mMotionScheme.fastEffectsSpec()
    )

    Box(
        modifier = modifier
            .padding(TabBoxPadding)
            .clip(shape)
            .background(animatedBGColor)
            .clickable(onClick = onClick)
    ) {
        Text(
            style = mTypography.bodyLarge,
            color = animatedContentColor,
            text = text,
            modifier = Modifier.padding(TabTextPadding)
        )
    }
}
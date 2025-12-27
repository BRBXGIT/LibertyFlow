@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design_system.containers.AnimatedBorderContainer
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import com.example.home.R
import com.example.home.screen.HomeIntent

private val ButtonLabel = R.string.random_button_label
internal const val RANDOM_BUTTON_KEY = "RANDOM_BUTTON_KEY"
private const val RANDOM_BUTTON_ICON_SIZE = 22
private const val CONTENT_ROW_PADDING_ARRANGEMENT = 8

@Composable
fun LazyGridItemScope.RandomAnimeButton(
    onIntent: (HomeIntent) -> Unit,
    showAnimation: Boolean
) {
    AnimatedBorderContainer(
        onClick = { onIntent(HomeIntent.GetRandomAnime) },
        modifier = Modifier.animateItem(),
        shape = mShapes.extraLarge,
        brush = Brush.sweepGradient(
            colors = listOf(
                getAnimatedColor(Color(0xFFE57373), showAnimation),
                getAnimatedColor(Color(0xFFFFB74D), showAnimation),
                getAnimatedColor(Color(0xFFFFF176), showAnimation),
                getAnimatedColor(Color(0xFF81C784), showAnimation),
                getAnimatedColor(Color(0xFF64B5F6), showAnimation),
                getAnimatedColor(Color(0xFFBA68C8), showAnimation)
            )
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CONTENT_ROW_PADDING_ARRANGEMENT.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(CONTENT_ROW_PADDING_ARRANGEMENT.dp)
        ) {
            Icon(
                painter = painterResource(LibertyFlowIcons.FunnyCube),
                contentDescription = null,
                modifier = Modifier.size(RANDOM_BUTTON_ICON_SIZE.dp)
            )

            Text(
                text = stringResource(ButtonLabel),
                style = mTypography.bodyMedium
            )
        }
    }
}

@Composable
private fun getAnimatedColor(
    color: Color,
    visible: Boolean
): Color {
    val animated by animateColorAsState(
        targetValue = if (visible) color else color.copy(alpha = 0f),
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = "Animated button color"
    )

    return animated
}
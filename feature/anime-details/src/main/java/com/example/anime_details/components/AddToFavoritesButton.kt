@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.anime_details.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
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
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.design_system.containers.AnimatedBorderContainer
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

private val ButtonLabel = R.string.add_to_favorites_button_label

internal const val ADD_TO_FAVORITE_BUTTON_KEY = "ADD_TO_FAVORITE_BUTTON_KEY"
private const val ANIMATED_ALPHA_LABEL = "Animated border alpha"

private const val UNSELECTED_COLOR_ALPHA = 0f
private const val SELECTED_COLOR_ALPHA = 1f

private const val RANDOM_BUTTON_ICON_SIZE = 22
private const val CONTENT_ROW_PADDING_ARRANGEMENT = 8
private const val HORIZONTAL_PADDING = 16

@Composable
internal fun LazyItemScope.AddToFavoritesButton(
    onIntent: (AnimeDetailsIntent) -> Unit,
    showAnimation: Boolean
) {
    // TODO ADD LOGIC
    AnimatedBorderContainer(
        onClick = { onIntent(AnimeDetailsIntent.FetchAnime(0)) },
        shape = mShapes.extraLarge,
        brush = animatedBrush(showAnimation),
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING.dp)
        )
    {
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
private fun animatedAlpha(visible: Boolean): Float {
    val alpha by animateFloatAsState(
        targetValue = if (visible) SELECTED_COLOR_ALPHA else UNSELECTED_COLOR_ALPHA,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = ANIMATED_ALPHA_LABEL
    )
    return alpha
}

@Composable
private fun animatedBrush(
    showAnimation: Boolean
): Brush {
    val alpha = animatedAlpha(showAnimation)

    return Brush.sweepGradient(
        colors = listOf(
            Color(0xFFE57373).copy(alpha = alpha),
            Color(0xFFFFB74D).copy(alpha = alpha),
            Color(0xFFFFF176).copy(alpha = alpha),
            Color(0xFF81C784).copy(alpha = alpha),
            Color(0xFF64B5F6).copy(alpha = alpha),
            Color(0xFFBA68C8).copy(alpha = alpha)
        )
    )
}
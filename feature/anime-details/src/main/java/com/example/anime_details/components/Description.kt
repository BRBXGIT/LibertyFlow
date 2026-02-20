@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.anime_details.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.design_system.components.icons.LibertyFlowAnimatedIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mTypography

private val NoDescriptionProvidedLabelRes =
    R.string.no_description_provided_label

private const val HIDDEN_LINES = 5

internal const val DescriptionKey = "DescriptionKey"

/**
 * Displays an optional description with expandable behavior.
 *
 * - Shows parsed HTML description if available
 * - Applies gradient fade when collapsed
 * - Displays an animated expand/collapse arrow when text exists
 */
@Composable
internal fun LazyItemScope.Description(
    description: String?,
    isExpanded: Boolean,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    // Parse HTML description only once
    val annotatedDescription = description?.let(AnnotatedString::fromHtml)
    val hasDescription = !description.isNullOrBlank()

    // Animate gradient color based on expanded state
    val animatedGradientColor by animateColorAsState(
        animationSpec = mMotionScheme.fastEffectsSpec(),
        targetValue = if (isExpanded) mColors.onBackground else mColors.background,
        label = "Animated gradient color that covers description"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .animateItem()
            .animateContentSize()
            .padding(horizontal = mDimens.paddingMedium)
    ) {
        // Description text or fallback
        if (annotatedDescription != null) {
            ExpandableText(
                text = annotatedDescription,
                isExpanded = isExpanded,
                gradientEndColor = animatedGradientColor
            )
        } else {
            Text(
                text = stringResource(NoDescriptionProvidedLabelRes),
                style = mTypography.bodyLarge
            )
        }

        // Expand / collapse arrow (only if description exists)
        if (hasDescription) {
            ExpandCollapseArrow(
                isExpanded = isExpanded,
                onClick = { onIntent(AnimeDetailsIntent.ToggleIsDescriptionExpanded) }
            )
        }
    }
}

/**
 * Expandable text with gradient fade when collapsed.
 */
@Composable
private fun ExpandableText(
    text: AnnotatedString,
    isExpanded: Boolean,
    gradientEndColor: Color
) {
    Text(
        text = text,
        maxLines = if (isExpanded) Int.MAX_VALUE else HIDDEN_LINES,
        overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
        style = mTypography.bodyLarge.let { baseStyle ->
            if (isExpanded) {
                baseStyle
            } else {
                baseStyle.copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            mColors.onBackground,
                            gradientEndColor
                        )
                    )
                )
            }
        }
    )
}

/**
 * Animated arrow indicating expand / collapse state.
 */
@Composable
private fun ExpandCollapseArrow(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    LibertyFlowAnimatedIcon(
        iconRes = LibertyFlowIcons.Animated.ArrowUp,
        colorFilter = ColorFilter.tint(mColors.onBackground),
        isRunning = !isExpanded,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        )
    )
}

@Preview
@Composable
private fun DescriptionPreview() {
    LazyColumn {
        item {
            Description(
                description = "Some new desc",
                isExpanded = false,
                onIntent = {}
            )
        }
    }
}
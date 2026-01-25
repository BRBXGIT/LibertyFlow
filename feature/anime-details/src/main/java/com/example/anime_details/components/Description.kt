package com.example.anime_details.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mTypography

private val NO_DESCRIPTION_PROVIDED_STRING =
    R.string.no_description_provided_label

private const val ANIMATED_COLOR_LABEL = "Animated color"
private const val HIDDEN_LINES = 5
private const val HORIZONTAL_PADDING = 16

internal const val DESCRIPTION_KEY = "DESCRIPTION_KEY"

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
        targetValue = if (isExpanded) {
            mColors.onBackground
        } else {
            mColors.background
        },
        label = ANIMATED_COLOR_LABEL
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .animateItem()
            .animateContentSize()
            .padding(horizontal = HORIZONTAL_PADDING.dp)
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
                text = stringResource(NO_DESCRIPTION_PROVIDED_STRING),
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
    val animatedImage = AnimatedImageVector.animatedVectorResource(
        LibertyFlowIcons.ArrowUpAnimated
    )

    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = animatedImage,
        atEnd = !isExpanded
    )

    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(mColors.onBackground),
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
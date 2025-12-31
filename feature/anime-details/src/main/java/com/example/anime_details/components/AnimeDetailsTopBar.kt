@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.example.anime_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.ui_helpers.UiEffect
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mTypography
import kotlinx.coroutines.delay

// Static error title text
private const val ERROR_TEXT = "Error"

// Fully transparent top bar background
private const val TOP_BAR_ALPHA = 0f

// Title text configuration
private const val TEXT_MAX_LINES = 1

// Navigation icon size
private const val ICON_SIZE = 22

@Composable
fun AnimeDetailsTopBar(
    isError: Boolean,
    englishTitle: String?,
    isLoading: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onEffect: (UiEffect) -> Unit
) {
    // Determines when the title should be visible
    val showTitle = !isLoading && englishTitle != null && !isError

    TopAppBar(
        title = {
            // Loading state
            AnimatedTopBarContent(visible = isLoading) {
                AnimatedLoadingText()
            }

            // Normal title state
            AnimatedTopBarContent(visible = showTitle) {
                Text(
                    text = englishTitle!!,
                    maxLines = TEXT_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                    style = mTypography.titleLarge
                )
            }

            // Error state
            AnimatedTopBarContent(visible = isError) {
                Text(text = ERROR_TEXT, style = mTypography.titleLarge)
            }
        },
        navigationIcon = {
            // Back navigation
            IconButton(onClick = { onEffect(UiEffect.NavigateBack) }) {
                Icon(
                    painter = painterResource(LibertyFlowIcons.ArrowLeftFilled),
                    contentDescription = null,
                    modifier = Modifier.size(ICON_SIZE.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = mColors.surfaceContainer.copy(alpha = TOP_BAR_ALPHA),
        ),
        scrollBehavior = scrollBehavior
    )
}

// Animation timing constants
private const val ANIMATION_DURATION = 300
private const val OFFSET_DIVIDER = 2

@Composable
private fun AnimatedTopBarContent(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    // Vertical slide + fade animation for top bar content
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetY = { it / OFFSET_DIVIDER }
        ) + fadeIn(tween(ANIMATION_DURATION)),
        exit = slideOutVertically(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetY = { -it / OFFSET_DIVIDER }
        ) + fadeOut(tween(ANIMATION_DURATION))
    ) {
        content()
    }
}

// Loading animation timing
private const val DELAY = 200L

// Dot animation configuration
private const val PLUS_DOT_COUNT = 1
private const val START_DOT_COUNT = 0
private const val DIVIDER = 4
private const val LIST_SIZE = 3

// Alpha values for dot visibility
private const val VISIBLE_ALPHA = 1f
private const val INVISIBLE_ALPHA = 0f

// Loading text content
private const val LOADING_TEXT = "Loading"
private const val DOT = "."

private const val ANIMATION_LABEL = "Dot alpha animation"

@Composable
private fun AnimatedLoadingText() {
    // Controls how many dots are visible
    var dotCount by rememberSaveable { mutableIntStateOf(START_DOT_COUNT) }

    // Cycles dot visibility
    LaunchedEffect(Unit) {
        while (true) {
            delay(DELAY)
            dotCount = (dotCount + PLUS_DOT_COUNT) % DIVIDER
        }
    }

    // Animates alpha for each dot
    val animatedDots = List(LIST_SIZE) { index ->
        animateFloatAsState(
            targetValue = if (index < dotCount) VISIBLE_ALPHA else INVISIBLE_ALPHA,
            animationSpec = mMotionScheme.fastEffectsSpec(),
            label = ANIMATION_LABEL
        )
    }

    Row {
        Text(text = LOADING_TEXT, style = mTypography.titleLarge)
        animatedDots.forEach { alpha ->
            Text(
                text = DOT,
                modifier = Modifier.alpha(alpha.value),
            )
        }
    }
}

@Preview
@Composable
private fun AnimatedLoadingTextPreview() {
    LibertyFlowTheme {
        AnimatedLoadingText()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AnimeScreenTopBaPreview() {
    LibertyFlowTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        AnimeDetailsTopBar(
            englishTitle = "Death Note",
            isLoading = true,
            scrollBehavior = scrollBehavior,
            isError = false,
            onEffect = {},
        )
    }
}
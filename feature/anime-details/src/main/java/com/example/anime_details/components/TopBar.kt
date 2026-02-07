@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.example.anime_details.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.containers.UpDownAnimatedContent
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mTypography
import kotlinx.coroutines.delay

private sealed interface TopBarState {
    data object Loading: TopBarState
    data object Error: TopBarState
    data class Content(val title: String): TopBarState
    data object Empty: TopBarState
}

// Static error title text
private val ErrorLabelRes = R.string.error_section_label

// Fully transparent top bar background
private const val TOP_BAR_ALPHA = 0f

@Composable
internal fun TopBar(
    isError: Boolean,
    englishTitle: String?,
    isLoading: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onEffect: (UiEffect) -> Unit
) {
    val currentState = when {
        isLoading -> TopBarState.Loading
        isError -> TopBarState.Error
        englishTitle != null -> TopBarState.Content(englishTitle)
        else -> TopBarState.Empty
    }

    TopAppBar(
        title = {
            // TODO: maybe change to older version with AnimatedVisibility
            UpDownAnimatedContent(targetState = currentState) { state ->
                when (state) {
                    is TopBarState.Loading -> AnimatedLoadingText()
                    is TopBarState.Error -> TopBarText(text = stringResource(ErrorLabelRes))
                    is TopBarState.Content -> TopBarText(text = state.title)
                    TopBarState.Empty -> Spacer(Modifier)
                }
            }
        },
        navigationIcon = {
            // Back navigation
            TopBarIconButton(
                icon = LibertyFlowIcons.ArrowLeftFilled,
                onClick = { onEffect(UiEffect.NavigateBack) }
            )
        },
        actions = {
            // TODO: Add lists logic
            TopBarIconButton(
                icon = LibertyFlowIcons.List,
                onClick = {}
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = mColors.surfaceContainer.copy(alpha = TOP_BAR_ALPHA),
        ),
        scrollBehavior = scrollBehavior
    )
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
private val LOADING_TEXT = R.string.loading_label
private const val DOT = "."

private const val ANIMATION_LABEL = "Dot alpha animation"

@Composable
private fun AnimatedLoadingText() {
    // Controls how many dots are visible
    var dotCount by remember { mutableIntStateOf(START_DOT_COUNT) }

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
        Text(text = stringResource(LOADING_TEXT), style = mTypography.titleLarge)
        animatedDots.forEach { alpha ->
            Text(
                text = DOT,
                modifier = Modifier.alpha(alpha.value),
            )
        }
    }
}

// Title text configuration
private const val TEXT_MAX_LINES = 1

@Composable
private fun TopBarText(text: String) {
    Text(
        text = text,
        maxLines = TEXT_MAX_LINES,
        overflow = TextOverflow.Ellipsis,
        style = mTypography.titleLarge
    )
}

// Navigation icon size
private const val ICON_SIZE = 22

@Composable
private fun TopBarIconButton(
    icon: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(ICON_SIZE.dp)
        )
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
private fun AnimeScreenTopBarPreview() {
    LibertyFlowTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        TopBar(
            englishTitle = "Death Note",
            isLoading = true,
            scrollBehavior = scrollBehavior,
            isError = false,
            onEffect = {},
        )
    }
}
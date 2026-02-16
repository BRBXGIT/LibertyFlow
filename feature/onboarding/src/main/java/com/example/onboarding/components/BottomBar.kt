@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.onboarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mMotionScheme
import com.example.onboarding.screen.OnboardingIntent
import com.example.onboarding.screen.OnboardingState
import kotlinx.coroutines.launch

private const val ICON_WEIGHT = 0.2f
private const val PROGRESS_WEIGHT = 0.6f
private const val INDICATOR_ALPHA = 0.1f
private const val DISABLED_ICON_ALPHA = 0.3f

private const val LAST_PAGE = 2

/**
 * A custom bottom bar for the Onboarding flow featuring a progress indicator
 * and directional navigation buttons.
 *
 * This component manages:
 * 1. **Backward Navigation**: Moves the [pagerState] to the previous index.
 * 2. **Progress Visualization**: A [LinearProgressIndicator] that animates based on [currentPage].
 * 3. **Forward/Finish Logic**: Advances the pager or dispatches [OnboardingIntent.SaveOnboardingCompleted]
 * when the final page is reached.
 *
 * @param currentPage The current active page index from the Pager.
 * @param totalPages The total number of pages in the onboarding sequence.
 * @param state The current [OnboardingState] to check business logic (e.g., permission status).
 * @param pagerState The state object controlling the [androidx.compose.foundation.pager.HorizontalPager].
 * @param onIntent Callback to dispatch [OnboardingIntent] actions to the ViewModel.
 */
@Composable
fun BottomBar(
    currentPage: Int,
    totalPages: Int,
    state: OnboardingState,
    pagerState: PagerState,
    onIntent: (OnboardingIntent) -> Unit
) {
    val targetProgress by remember(currentPage, totalPages) {
        derivedStateOf { (currentPage + 1).toFloat() / totalPages }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = mMotionScheme.slowSpatialSpec(),
        label = "Animated indicator progress"
    )

    val animationScope = rememberCoroutineScope()
    BottomAppBar(
        containerColor = Color.Transparent,
        contentPadding = PaddingValues(horizontal = mDimens.paddingMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(mDimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.weight(ICON_WEIGHT),
                onClick = {
                    animationScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = currentPage > 0
            ) {
                Icon(
                    painter = painterResource(LibertyFlowIcons.Filled.AltArrowLeft),
                    contentDescription = null,
                    tint = if (currentPage > 0) mColors.primary else mColors.onSurface.copy(alpha = DISABLED_ICON_ALPHA)
                )
            }

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .weight(PROGRESS_WEIGHT)
                    .height(mDimens.spacingSmall)
                    .clip(CircleShape),
                color = mColors.primary,
                trackColor = mColors.primary.copy(alpha = INDICATOR_ALPHA)
            )

            val enabled = currentPage != LAST_PAGE || state.triedToAskPermission
            IconButton(
                enabled = enabled,
                modifier = Modifier.weight(ICON_WEIGHT),
                onClick = {
                    if (pagerState.currentPage < totalPages - 1) {
                        animationScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onIntent(OnboardingIntent.SaveOnboardingCompleted)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(if (currentPage < LAST_PAGE) LibertyFlowIcons.Filled.AltArrowRight else LibertyFlowIcons.Filled.DoubleCheck),
                    contentDescription = null,
                    tint = if (enabled) mColors.primary else mColors.onSurface.copy(alpha = DISABLED_ICON_ALPHA)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    // Mocking the state for the first page
    LibertyFlowTheme {
        BottomBar(
            currentPage = 0,
            totalPages = 3,
            state = OnboardingState(triedToAskPermission = false),
            pagerState = rememberPagerState { 3 },
            onIntent = {}
        )
    }
}
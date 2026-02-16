package com.example.onboarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.theme.theme.mColors
import com.example.onboarding.components.BottomBar
import com.example.onboarding.components.OnboardingPageContent

private val onboardingPages = listOf(
    OnboardingPage.Welcome,
    OnboardingPage.Vpn,
    OnboardingPage.Permissions
)

/**
 * The root Composable for the Onboarding screen, implementing a pager-based navigation flow.
 *
 * This component coordinates the following:
 * 1. **Pager Logic**: Manages a [HorizontalPager] using the defined [onboardingPages].
 * 2. **Visual Style**: Applies a vertical gradient background with the brand's primary color.
 * 3. **Navigation UI**: Displays a custom [BottomBar] that synchronizes with the pagerState.
 * 4. **Adaptive Content**: Dynamically injects [OnboardingPageContent] for each step of the flow.
 *
 * @param state The current immutable UI state.
 * @param onIntent Lambda to dispatch actions to the business logic layer.
 * @param onEffect Lambda to trigger one-time UI side effects (e.g., snackbars).
 */
@Composable
fun Onboarding(
    state: OnboardingState,
    onIntent: (OnboardingIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val pagerState = rememberPagerState { onboardingPages.size }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = mColors.background,
        bottomBar = {
            BottomBar(
                currentPage = pagerState.currentPage,
                totalPages = onboardingPages.size,
                pagerState = pagerState,
                state = state,
                onIntent = onIntent,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(mColors.primary, mColors.background)
                    )
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { pageIndex ->
                OnboardingPageContent(
                    onIntent = onIntent,
                    page = onboardingPages[pageIndex],
                    modifier = Modifier.fillMaxSize(),
                    onEffect = onEffect
                )
            }
        }
    }
}
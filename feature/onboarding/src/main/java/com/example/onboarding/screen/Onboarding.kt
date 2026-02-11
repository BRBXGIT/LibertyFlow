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
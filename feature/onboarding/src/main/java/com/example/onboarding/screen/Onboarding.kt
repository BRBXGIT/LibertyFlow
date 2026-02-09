package com.example.onboarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.theme.theme.mColors
import com.example.onboarding.components.BottomBar
import com.example.onboarding.components.OnboardingPageContent
import kotlinx.coroutines.launch

private val onboardingPages = listOf(
    OnboardingPage.Welcome,
    OnboardingPage.Vpn,
    OnboardingPage.Permissions
)

private const val NEXT_PAGE_PLUS = 1

@Composable
fun Onboarding(
    onIntent: (OnboardingIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val pagerState = rememberPagerState { onboardingPages.size }
    val animationScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = mColors.background,
        bottomBar = {
            BottomBar(
                currentPage = pagerState.currentPage,
                totalPages = onboardingPages.size,
                onBackClick = {
                    animationScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - NEXT_PAGE_PLUS)
                    }
                },
                onNextClick = {
                    if (pagerState.currentPage < onboardingPages.size - NEXT_PAGE_PLUS) {
                        animationScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + NEXT_PAGE_PLUS)
                        }
                    } else {
                        onIntent(OnboardingIntent.SaveOnboardingCompleted)
                    }
                }
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
                    page = onboardingPages[pageIndex],
                    modifier = Modifier.fillMaxSize(),
                    onEffect = onEffect
                )
            }
        }
    }
}
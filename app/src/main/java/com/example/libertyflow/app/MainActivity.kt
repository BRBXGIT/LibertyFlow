package com.example.libertyflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.navigation.HomeRoute
import com.example.common.navigation.OnboardingRoute
import com.example.data.models.onboarding.OnboardingState
import com.example.design_system.theme.logic.ThemeIntent
import com.example.design_system.theme.logic.ThemeVM
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.libertyflow.R
import com.example.libertyflow.app_starting.AppStartingVM
import com.example.libertyflow.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * The primary entry point for the application.
 * * This Activity manages the initial app startup flow, including:
 * 1. **Splash Screen Management**: Keeps the splash screen visible until onboarding state is loaded.
 * 2. **Dependency Injection**: Integrated with Hilt via [AndroidEntryPoint].
 * 3. **Dynamic Theming**: Synchronizes system dark mode changes with the [ThemeVM] and applies
 * the custom [LibertyFlowTheme].
 * 4. **Navigation Routing**: Determines the initial destination ([HomeRoute] vs [OnboardingRoute])
 * based on the user's onboarding status.
 */
@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Avoid bug when theme doesn't change after splashscreen
        setTheme(R.style.Theme_LibertyFlow)

        enableEdgeToEdge()
        val splashScreen = installSplashScreen()

        setContent {
            val appStartingVM = hiltViewModel<AppStartingVM>()
            val appStartingState by appStartingVM.appStartingState.collectAsStateWithLifecycle()

            splashScreen.setKeepOnScreenCondition {
                appStartingState.onboardingCompleted is OnboardingState.Loading
            }

            val themeVM = hiltViewModel<ThemeVM>()
            val isSystemDark = isSystemInDarkTheme()

            // React to system theme changes (e.g. user toggles Quick Settings)
            // This ensures the flow inside VM updates immediately.
            LaunchedEffect(isSystemDark) {
                themeVM.sendIntent(ThemeIntent.UpdateSystemDarkMode(isSystemDark))
            }

            // Collect the calculated state
            val themeState by themeVM.themeState.collectAsStateWithLifecycle()

            LibertyFlowTheme(
                useExpressive = themeState.useExpressive,
                colorScheme = themeState.activeColorScheme,
                theme = themeState.userThemePreference
            ) {
                if (appStartingState.onboardingCompleted !is OnboardingState.Loading) {
                    NavGraph(
                        themeVM = themeVM,
                        startDestination = when(appStartingState.onboardingCompleted) {
                            OnboardingState.Completed -> HomeRoute
                            else -> OnboardingRoute
                        }
                    )
                }
            }
        }
    }
}
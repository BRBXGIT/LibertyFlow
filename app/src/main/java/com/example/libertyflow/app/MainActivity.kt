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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Avoid bug when theme doesn't want to change after splashscreen
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
                colorScheme = themeState.activeColorScheme
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
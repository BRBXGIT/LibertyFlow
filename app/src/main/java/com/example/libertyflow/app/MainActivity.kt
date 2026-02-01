package com.example.libertyflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.design_system.theme.logic.ThemeIntent
import com.example.design_system.theme.logic.ThemeVM
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.libertyflow.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
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
                NavGraph(themeVM)
            }
        }
    }
}
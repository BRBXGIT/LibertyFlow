package com.example.libertyflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.design_system.theme.LibertyFlowTheme
import com.example.libertyflow.navigation.NavGraph
import com.example.libertyflow.theme.ThemeIntent
import com.example.libertyflow.theme.ThemeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeVM = hiltViewModel<ThemeVM>()
            ObserveTheme(themeVM::sendIntent, isSystemInDarkTheme())
            val themeState by themeVM.themeState.collectAsStateWithLifecycle()

            LibertyFlowTheme(
                useExpressive = themeState.useExpressive,
                colorScheme = themeState.colorScheme
            ) {
                NavGraph()
            }
        }
    }
}

@Composable
private fun ObserveTheme(
    intent: (ThemeIntent) -> Unit,
    isSystemInDarkMode: Boolean
) {
    LaunchedEffect(Unit) {
        intent(ThemeIntent.ObserveTheme(isSystemInDarkMode))
    }
}
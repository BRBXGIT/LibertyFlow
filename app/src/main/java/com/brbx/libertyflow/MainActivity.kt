package com.brbx.libertyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.brbx.design_system.theme.Typography
import com.brbx.ui_compose.theme.BrbxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scheme = if (isSystemInDarkTheme()) {
                darkColorScheme()
            } else {
                lightColorScheme()
            }
            BrbxTheme(
                colorScheme = scheme,
                typography = Typography,
            ) {
                NavGraph()
            }
        }
    }
}
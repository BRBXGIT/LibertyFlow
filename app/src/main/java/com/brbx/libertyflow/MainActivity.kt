package com.brbx.libertyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.darkColorScheme
import com.brbx.ui_compose.theme.BrbxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrbxTheme(darkColorScheme()) {
                NavGraph()
            }
        }
    }
}
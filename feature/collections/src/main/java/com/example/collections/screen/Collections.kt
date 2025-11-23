package com.example.collections.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Collections() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Text(
            text = "Collections",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
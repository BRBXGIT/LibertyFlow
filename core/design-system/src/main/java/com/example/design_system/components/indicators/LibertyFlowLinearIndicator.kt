@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.indicators

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val HORIZONTAL_PADDING = 16

@Composable
fun LibertyFlowLinearIndicator() {
    LinearWavyProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING.dp)
    )
}
package com.example.design_system.components.indicators

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private object LinearIndicatorConstants {
    const val HORIZONTAL_PADDING = 16
}

@Composable
fun LibertyFlowLinearIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LinearIndicatorConstants.HORIZONTAL_PADDING.dp)
    )
}
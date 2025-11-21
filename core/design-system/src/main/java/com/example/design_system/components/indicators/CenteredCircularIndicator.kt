@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.indicators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.theme.LibertyFlowTheme

@Composable
fun CenteredCircularIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ContainedLoadingIndicator()
    }
}

@Preview
@Composable
private fun CenteredCircularIndicatorPreview() {
    LibertyFlowTheme {
        CenteredCircularIndicator()
    }
}
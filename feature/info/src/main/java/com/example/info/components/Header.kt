package com.example.info.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.LibertyFlowIcons

private val MAIN_BOX_HEIGHT = 210.dp
private val MAIN_PADDING = 16.dp
private val ICON_SIZE = 84.dp

@Composable
internal fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MAIN_BOX_HEIGHT)
            .padding(horizontal = MAIN_PADDING)
    ) {
        Icon(
            painter = painterResource(LibertyFlowIcons.LibertyFlow),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.Center)
                .size(ICON_SIZE),
        )

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}
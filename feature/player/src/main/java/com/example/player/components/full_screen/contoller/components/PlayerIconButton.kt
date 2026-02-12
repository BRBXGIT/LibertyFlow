package com.example.player.components.full_screen.contoller.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
internal fun PlayerIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified,
    isEnabled: Boolean = true,
    isAvailable: Boolean = true
) {
    IconButton(
        onClick = { if (isAvailable) onClick() },
        modifier = modifier,
        enabled = isEnabled
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = if (iconSize != Dp.Unspecified) Modifier.size(iconSize) else Modifier,
            tint = if (isAvailable) Color.White else Color.Gray
        )
    }
}
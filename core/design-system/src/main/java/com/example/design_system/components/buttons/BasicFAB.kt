package com.example.design_system.components.buttons

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun BasicFAB(
    modifier: Modifier = Modifier,
    icon: Int,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null
        )
    }
}
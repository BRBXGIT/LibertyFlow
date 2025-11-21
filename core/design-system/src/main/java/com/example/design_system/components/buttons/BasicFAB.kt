package com.example.design_system.components.buttons

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun BasicFAB(
    icon: Int,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null
        )
    }
}